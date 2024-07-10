package br.com.univates.ecoleta.layout.navigation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.layout.agendamento.NewAgendamento;
import br.com.univates.ecoleta.layout.ponto_coleta.NewPontoColeta;
import br.com.univates.ecoleta.utils.EcoletaUtils;


public class NavigationAdd extends Fragment {

    public NavigationAdd() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_add, container, false);

        view.findViewById(R.id.btnCadPontoColeta).setOnClickListener(v -> {
            EcoletaUtils.replaceFragment(NewPontoColeta.class, true, requireActivity().getSupportFragmentManager(),true);
        });

        view.findViewById(R.id.btnSolicitarColeta).setOnClickListener(v -> {
            EcoletaUtils.replaceFragment(NewAgendamento.class, true, requireActivity().getSupportFragmentManager(),true);
        });
        
        return view;
    }

}