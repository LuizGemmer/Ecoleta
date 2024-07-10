package br.com.univates.ecoleta;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import br.com.univates.ecoleta.databinding.ActivityMainBinding;
import br.com.univates.ecoleta.layout.navigation.NavigationAdd;
import br.com.univates.ecoleta.layout.navigation.NavigationHome;
import br.com.univates.ecoleta.layout.navigation.NavigationItem;
import br.com.univates.ecoleta.layout.navigation.NavigationSearch;
import br.com.univates.ecoleta.layout.navigation.NavigationUser;
import br.com.univates.ecoleta.utils.EcoletaUtils;

public class MainActivityPrincipal extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura o listener para o BottomNavigationView
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                replaceFragment(NavigationHome.class);
            } else if (itemId == R.id.navigation_search) {
                replaceFragment(NavigationSearch.class);
            } else if (itemId == R.id.navigation_itemType) {
                replaceFragment(NavigationItem.class);
            } else if (itemId == R.id.navigation_user) {
                replaceFragment(NavigationUser.class);
            } else if (itemId == R.id.navigation_add) {
                replaceFragment(NavigationAdd.class);
            }
            return true;
        });

        // Configura o listener para o FloatingActionButton
        binding.addFabBtn.setOnClickListener(view -> {
            replaceFragment(NavigationAdd.class);
        });
    }

    public <T extends Fragment> void replaceFragment(Class<T> fragmentClass) {
        EcoletaUtils.replaceFragment(fragmentClass, true, getSupportFragmentManager(),false);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    // Método para adicionar um fragmento à pilha
    public void addFragmentToBackStack(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentPrincipal, fragment)
                .addToBackStack(null)
                .commit();
    }
}
