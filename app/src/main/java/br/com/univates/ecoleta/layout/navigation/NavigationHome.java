package br.com.univates.ecoleta.layout.navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.db.entity.Coleta;
import br.com.univates.ecoleta.db.service.ColetaService;
import br.com.univates.ecoleta.layout.agendamento.AgendamentoAdapter;
import br.com.univates.ecoleta.layout.agendamento.AgendamentoEditModal;

public class NavigationHome extends Fragment implements AgendamentoEditModal.OnColetaUpdatedListener {

    private AgendamentoAdapter adapter;
    private List<Coleta> coletaList;
    private ColetaService coletaService;

    public NavigationHome() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coletaService = new ColetaService();
        coletaList = new ArrayList<>();
        adapter = new AgendamentoAdapter(coletaList, coletaService, requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadColetaList();

        adapter.setOnItemClickListener(new AgendamentoAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                adapter.removeItem(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onEditClick(int position) {
                Coleta coleta = coletaList.get(position);
                openEditModal(coleta);
            }
        });

        return view;
    }

    private void openEditModal(Coleta coleta) {
        AgendamentoEditModal modalFragment = new AgendamentoEditModal(coleta, coletaService);
        modalFragment.setTargetFragment(this, 0); // Configurar o fragmento pai como alvo para receber resultados
        modalFragment.show(getParentFragmentManager(), "EditColetaModal");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadColetaList() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Coleta> listaPontosColeta = coletaService.findAll();
            requireActivity().runOnUiThread(() -> {
                coletaList.clear();
                coletaList.addAll(listaPontosColeta);
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    public void onColetaUpdated(Coleta coleta) {
        // Atualizar a lista de coletas após uma coleta ser atualizada no modal
        int position = coletaList.indexOf(coleta);
        if (position != -1) {
            coletaList.set(position, coleta);
            adapter.notifyItemChanged(position);
        }
    }
}
