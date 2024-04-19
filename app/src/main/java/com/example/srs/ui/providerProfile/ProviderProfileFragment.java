package com.example.srs.ui.providerProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.srs.MainActivity;
import com.example.srs.R;
import com.example.srs.databinding.FragmentProviderProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProviderProfileFragment extends Fragment {

    Button button;
    private FragmentProviderProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProviderProfileViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProviderProfileViewModel.class);


        binding = FragmentProviderProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProviderProfile;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        button = root.findViewById(R.id.logout_provider);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return root;



    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}