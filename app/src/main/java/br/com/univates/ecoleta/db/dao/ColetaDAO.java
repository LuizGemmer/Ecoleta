package br.com.univates.ecoleta.db.entity.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.univates.ecoleta.db.entity.Coleta;

public class ColetaDAO {

    private static DatabaseReference databaseReference;

    public ColetaDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference("coletas");
    }

    public static String getNextId() {
        return databaseReference.push().getKey();
    }

    public static void salvar(Coleta coleta) {
        databaseReference.child(coleta.getId()).setValue(coleta);
    }

    public static void atualizar(Coleta coleta) {
        databaseReference.child(coleta.getId()).setValue(coleta);
    }

    public static void remover(String idColeta) {
        databaseReference.child(idColeta).removeValue();
    }


    public void remover(String id, final OnColetaRemovidaListener listener) {
        databaseReference.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    listener.onColetaRemovida(true); // Remoção bem-sucedida
                } else {
                    listener.onColetaRemovida(false); // Erro ao remover
                }
            }
        });
    }

    public static void listar(final OnColetasCarregadasListener listener) {
        // Adiciona um listener para recuperar os dados do nó "coletas"
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Coleta> listaColetas = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Coleta coleta = snapshot.getValue(Coleta.class);
                    listaColetas.add(coleta);
                }
                listener.onColetasCarregadas(listaColetas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tratamento de erro
            }
        });
    }

    public interface OnColetasCarregadasListener {
        void onColetasCarregadas(List<Coleta> coletas);
    }

    public interface OnColetaRemovidaListener {
        void onColetaRemovida(boolean sucesso);
    }

}