package br.com.univates.ecoleta.db.dao;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

import br.com.univates.ecoleta.db.entity.Usuario;

public class UsuarioDAO {

    private static DatabaseReference databaseReference;

    public UsuarioDAO() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("usuario");
        }
    }

    public String getNextId() {
        return databaseReference.push().getKey();
    }

    public void salvar(Usuario usuario) {
        if(usuario.getId() == null)
            usuario.setId(getNextId());

        databaseReference.child(usuario.getId()).setValue(usuario);
    }

    public void atualizar(Usuario usuario) {
        databaseReference.child(usuario.getId()).setValue(usuario);
    }

    public void remover(String idUsuario) {
        databaseReference.child(idUsuario).removeValue();
    }

    public Usuario findById(String idUsuario) {
        final Usuario[] usuario = new Usuario[1];
        final CountDownLatch latch = new CountDownLatch(1);

        databaseReference.child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    usuario[0] = dataSnapshot.getValue(Usuario.class);
                    if (usuario[0] != null) {
                        usuario[0].setId(dataSnapshot.getKey());
                    }
                    latch.countDown();
                } else {
                    usuario[0] = null;
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

        return usuario[0];
    }
}
