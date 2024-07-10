package br.com.univates.ecoleta.layout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import br.com.univates.ecoleta.MainActivityPrincipal;
import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.utils.EcoletaUtils;


public class NavigationAdd extends Fragment {

    public NavigationAdd() {}

    public static NavigationAdd newInstance() {
        NavigationAdd fragment = new NavigationAdd();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_add, container, false);

        view.findViewById(R.id.btnCadPontoColeta).setOnClickListener(v -> {
            EcoletaUtils.replaceFragment(NewPontoColeta.class, true, requireActivity().getSupportFragmentManager());
        });

        view.findViewById(R.id.btnSolicitarColeta).setOnClickListener(v -> {});
        
        return view;
    }

}