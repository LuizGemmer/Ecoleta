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

public class MainActivityPrincipal extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new NavigationHome());
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                replaceFragment(new NavigationHome());
            } else if (itemId == R.id.navigation_search) {
                replaceFragment(new NavigationSearch());
            } else if (itemId == R.id.navigation_itemType) {
                replaceFragment(new NavigationItem());
            } else if (itemId == R.id.navigation_user) {
                replaceFragment(new NavigationUser());
            }
            return true;
        });

        binding.addFabBtn.setOnClickListener(view -> {
            replaceFragment(new NavigationAdd());
        });

    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bottomFragment, fragment);
        fragmentTransaction.commit();
    }
}
