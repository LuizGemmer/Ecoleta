package br.com.univates.ecoleta.layout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.univates.ecoleta.R;

public class NavigationHome extends Fragment {

    public NavigationHome() {}

    public static NavigationHome newInstance(String param1, String param2) {
        NavigationHome fragment = new NavigationHome();
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
        return inflater.inflate(R.layout.navigation_home, container, false);
    }
}