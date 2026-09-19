package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ForgotPasswordFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        View btnReset = view.findViewById(R.id.btn_send_reset_link);
        if (btnReset != null) {
            btnReset.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_forgotPassword_to_login)
            );
        }

        View tvBack = view.findViewById(R.id.tv_back_to_login);
        if (tvBack != null) {
            tvBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_forgotPassword_to_login)
            );
        }

        return view;
    }
}