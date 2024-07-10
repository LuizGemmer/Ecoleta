package br.com.univates.ecoleta.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;

import br.com.univates.ecoleta.MainActivityPrincipal;
import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.layout.ponto_coleta.NewPontoColetaFinal;

public class EcoletaUtils {

    public static <T extends Fragment> void replaceFragment(Class<T> minhaClasse, boolean addToBackStack, FragmentManager supportFragmentManager, boolean limparCahe) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();

        try {
            Fragment fragment = minhaClasse.newInstance();
            transaction.replace(R.id.fragmentPrincipal, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();

            if(limparCahe)
                supportFragmentManager.executePendingTransactions();

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void replaceFragment(Fragment fragment, boolean addToBackStack, FragmentManager supportFragmentManager) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentPrincipal, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public static void redirectToAnotherActivity(Context context, Class<?> mainClass) {
        Intent intent = new Intent(context, mainClass);
        context.startActivity(intent);
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            return String.format("%s", Base64.encodeToString(byteArray, Base64.DEFAULT));
        }

        return null;
    }
}
