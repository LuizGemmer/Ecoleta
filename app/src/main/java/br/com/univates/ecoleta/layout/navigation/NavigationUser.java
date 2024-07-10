package br.com.univates.ecoleta.layout.navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import br.com.univates.ecoleta.MainActivity;
import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.utils.EcoletaUtils;

public class NavigationUser extends Fragment {

    private FirebaseAuth mAuth;

    public NavigationUser() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_user, container, false);

        ImageView profileImageView = view.findViewById(R.id.profile_image);
        TextView emailTextView = view.findViewById(R.id.email_text_view);
        Button logoutButton = view.findViewById(R.id.logout_button);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            emailTextView.setText(user.getEmail());

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.user_solid)
                        .circleCrop()
                        .into(profileImageView);
            } else {
                profileImageView.setImageResource(R.drawable.user_solid);
            }
        }

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            EcoletaUtils.redirectToAnotherActivity(requireActivity(), MainActivity.class);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}
