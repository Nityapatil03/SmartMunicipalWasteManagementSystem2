package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        MaterialButtonToggleGroup toggleRole = view.findViewById(R.id.toggle_role);
        EditText etEmail = view.findViewById(R.id.et_login_email);

        if (toggleRole != null && etEmail != null) {
            toggleRole.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
                if (isChecked) {
                    if (checkedId == R.id.btn_role_admin) {
                        etEmail.setHint("e.g. admin@smartwaste.gov");
                    } else if (checkedId == R.id.btn_role_worker) {
                        etEmail.setHint("e.g. worker@smartwaste.gov");
                    } else {
                        etEmail.setHint("e.g. citizen@smartwaste.gov");
                    }
                }
            });
        }

        View btnLogin = view.findViewById(R.id.btn_login_submit);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                int checkedId = toggleRole != null ? toggleRole.getCheckedButtonId() : R.id.btn_role_citizen;
                if (checkedId == R.id.btn_role_admin) {
                    RoleManager.setActiveRole(requireContext(), RoleManager.Role.ADMIN);
                    Navigation.findNavController(v).navigate(R.id.action_login_to_adminDashboard);
                } else if (checkedId == R.id.btn_role_worker) {
                    RoleManager.setActiveRole(requireContext(), RoleManager.Role.WORKER);
                    Navigation.findNavController(v).navigate(R.id.action_login_to_workerTaskList);
                } else {
                    RoleManager.setActiveRole(requireContext(), RoleManager.Role.CITIZEN);
                    Navigation.findNavController(v).navigate(R.id.action_login_to_citizenDashboard);
                }
            });
        }

        View tvSignUp = view.findViewById(R.id.tv_go_signup);
        if (tvSignUp != null) {
            tvSignUp.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_login_to_signUp)
            );
        }

        View tvForgot = view.findViewById(R.id.tv_forgot_password);
        if (tvForgot != null) {
            tvForgot.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_login_to_forgotPassword)
            );
        }

        return view;
    }
}
