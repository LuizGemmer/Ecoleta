package br.com.univates.ecoleta.layout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.univates.ecoleta.R;


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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.navigation_add, container, false);

        view.findViewById(R.id.btnCadPontoColeta).setOnClickListener(v -> {
            replaceFragment(new NewPontoColeta());
        });

        view.findViewById(R.id.btnSolicitarColeta).setOnClickListener(v -> {

        });
        
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // ao adicionar uma nova tela sempre chamar o bottomFragment da MainActivityPrincipal
        transaction.replace(R.id.bottomFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}