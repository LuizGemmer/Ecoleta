package br.com.univates.ecoleta.db.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import br.com.univates.ecoleta.db.entity.Coleta;
import br.com.univates.ecoleta.db.entity.Usuario;

public class ColetaDAO {

    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("coletas");

    public static String getNextId() {
        return databaseReference.push().getKey();
    }

    public static void salvar(Coleta coleta) {
        if(coleta.getId() == null)
            coleta.setId(getNextId());

        databaseReference.child(coleta.getId()).setValue(coleta);
    }

    public static void atualizar(Coleta coleta) {
        databaseReference.child(coleta.getId()).setValue(coleta);
    }

    public static void remover(String idColeta) {
        databaseReference.child(idColeta).removeValue();
    }

    public Coleta findById(String idColeta) {
        final Coleta[] coleta = new Coleta[1];
        final CountDownLatch latch = new CountDownLatch(1);

        databaseReference.child(idColeta).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    coleta[0] = dataSnapshot.getValue(Coleta.class);
                    if (coleta[0] != null) {
                        coleta[0].setId(dataSnapshot.getKey());
                    }
                    latch.countDown();
                } else {
                    coleta[0] = null;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coleta[0];
    }

    public List<Coleta> findAll() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Coleta>> future = new CompletableFuture<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Coleta> coletas = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Coleta coleta = snapshot.getValue(Coleta.class);
                    if (coleta != null) {
                        coleta.setId(snapshot.getKey());
                        coletas.add(coleta);
                    }
                }
                future.complete(coletas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future.get();
    }
}