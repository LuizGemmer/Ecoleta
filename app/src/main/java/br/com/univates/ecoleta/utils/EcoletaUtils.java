package br.com.univates.ecoleta.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import br.com.univates.ecoleta.R;

public class EcoletaUtils {

    public static void replaceFragment(Fragment fragment, int fragmentPrincipal, FragmentManager parentFragmentManager) {
        FragmentTransaction transaction = parentFragmentManager.beginTransaction();
        // ao adicionar uma nova tela sempre chamar o fragmentPrincipal da MainActivityPrincipal
        transaction.replace(R.id.fragmentPrincipal, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
