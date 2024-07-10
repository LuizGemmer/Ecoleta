package br.com.univates.ecoleta.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.layout.NewPontoColetaFinal;

public class EcoletaUtils {

    public static <T extends Fragment> void replaceFragment(Class<T> minhaClasse, boolean addToBackStack, FragmentManager supportFragmentManager) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();

        try {
            Fragment fragment = minhaClasse.newInstance();
            transaction.replace(R.id.fragmentPrincipal, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
