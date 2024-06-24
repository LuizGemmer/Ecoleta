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

import br.com.univates.ecoleta.db.entity.TipoItem;

public class TipoItemDAO {

    private static DatabaseReference databaseReference;

    public TipoItemDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference("tipoItems");
    }

    public static String getNextId() {
        return databaseReference.push().getKey();
    }

    public static void salvar(TipoItem tipoItem) {
        databaseReference.child(tipoItem.getId()).setValue(tipoItem);
    }

    public static void atualizar(TipoItem tipoItem) {
        databaseReference.child(tipoItem.getId()).setValue(tipoItem);
    }

    public static void remover(String idTipoItem) {
        databaseReference.child(idTipoItem).removeValue();
    }


    public void remover(String id, final OnTipoItemRemovidaListener listener) {
        databaseReference.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    listener.onTipoItemRemovida(true); // Remoção bem-sucedida
                } else {
                    listener.onTipoItemRemovida(false); // Erro ao remover
                }
            }
        });
    }

    public static void listar(final OnTipoItemsCarregadasListener listener) {
        // Adiciona um listener para recuperar os dados do nó "tipoItems"
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TipoItem> listaTipoItems = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TipoItem tipoItem = snapshot.getValue(TipoItem.class);
                    listaTipoItems.add(tipoItem);
                }
                listener.onTipoItemsCarregadas(listaTipoItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tratamento de erro
            }
        });
    }

    public interface OnTipoItemsCarregadasListener {
        void onTipoItemsCarregadas(List<TipoItem> tipoItems);
    }

    public interface OnTipoItemRemovidaListener {
        void onTipoItemRemovida(boolean sucesso);
    }

}