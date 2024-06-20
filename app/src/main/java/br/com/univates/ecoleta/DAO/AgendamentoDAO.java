package br.com.univates.ecoleta.DAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.univates.ecoleta.models.Agendamento;

public class AgendamentoDAO {

    private static DatabaseReference databaseReference;

    public AgendamentoDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference("agendamentos");
    }

    public static String getNextId() {
        return databaseReference.push().getKey();
    }

    public static void salvar(Agendamento agendamento) {
        databaseReference.child(agendamento.getId()).setValue(agendamento);
    }

    public static void atualizar(Agendamento agendamento) {
        databaseReference.child(agendamento.getId()).setValue(agendamento);
    }

    public static void remover(String idAgendamento) {
        databaseReference.child(idAgendamento).removeValue();
    }


    public void remover(String id, final OnAgendamentoRemovidaListener listener) {
        databaseReference.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    listener.onAgendamentoRemovida(true); // Remoção bem-sucedida
                } else {
                    listener.onAgendamentoRemovida(false); // Erro ao remover
                }
            }
        });
    }

    public static void listar(final OnAgendamentosCarregadasListener listener) {
        // Adiciona um listener para recuperar os dados do nó "agendamentos"
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Agendamento> listaAgendamentos = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Agendamento agendamento = snapshot.getValue(Agendamento.class);
                    listaAgendamentos.add(agendamento);
                }
                listener.onAgendamentosCarregadas(listaAgendamentos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tratamento de erro
            }
        });
    }

    public interface OnAgendamentosCarregadasListener {
        void onAgendamentosCarregadas(List<Agendamento> agendamentos);
    }

    public interface OnAgendamentoRemovidaListener {
        void onAgendamentoRemovida(boolean sucesso);
    }

}