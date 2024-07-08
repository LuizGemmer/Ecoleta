package br.com.univates.ecoleta;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import br.com.univates.ecoleta.databinding.ActivityMainBinding;
import br.com.univates.ecoleta.layout.NavigationAdd;
import br.com.univates.ecoleta.layout.NavigationHome;
import br.com.univates.ecoleta.layout.NavigationItem;
import br.com.univates.ecoleta.layout.NavigationSearch;
import br.com.univates.ecoleta.layout.NavigationUser;
import br.com.univates.ecoleta.layout.NewPontoColetaFinal;

public class MainActivityPrincipal extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new NavigationHome(), false);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                replaceFragment(new NavigationHome(), false);
            } else if (itemId == R.id.navigation_search) {
                replaceFragment(new NavigationSearch(), false);
            } else if (itemId == R.id.navigation_itemType) {
                replaceFragment(new NavigationItem(), false);
            } else if (itemId == R.id.navigation_user) {
                replaceFragment(new NavigationUser(), false);
            }
            return true;
        });

        binding.addFabBtn.setOnClickListener(view -> {
            replaceFragment(new NavigationAdd(), true);
        });
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentPrincipal, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void navigateToPhotoDisplay(NewPontoColetaFinal ponto) {
        replaceFragment(ponto,true);
    }
}
