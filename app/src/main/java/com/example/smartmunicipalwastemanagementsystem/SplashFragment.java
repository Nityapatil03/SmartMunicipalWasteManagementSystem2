package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        View btnGetStarted = view.findViewById(R.id.btn_get_started);
        View cardCitizen = view.findViewById(R.id.card_portal_citizen);
        View cardAdmin = view.findViewById(R.id.card_portal_admin);
        View cardWorker = view.findViewById(R.id.card_portal_worker);

        View.OnClickListener goLoginListener = v ->
            Navigation.findNavController(v).navigate(R.id.action_splash_to_login);

        if (btnGetStarted != null) btnGetStarted.setOnClickListener(goLoginListener);
        if (cardCitizen != null) cardCitizen.setOnClickListener(goLoginListener);
        if (cardAdmin != null) cardAdmin.setOnClickListener(goLoginListener);
        if (cardWorker != null) cardWorker.setOnClickListener(goLoginListener);

        return view;
    }
}