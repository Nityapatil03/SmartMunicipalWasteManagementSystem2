package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SignUpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        EditText etName = view.findViewById(R.id.et_signup_name);
        EditText etEmail = view.findViewById(R.id.et_signup_email);

        View btnSignUp = view.findViewById(R.id.btn_signup_submit);
        if (btnSignUp != null) {
            btnSignUp.setOnClickListener(v -> {
                String nameStr = etName != null && etName.getText() != null ? etName.getText().toString() : "Citizen User";
                String emailStr = etEmail != null && etEmail.getText() != null ? etEmail.getText().toString() : "citizen@smartwaste.gov";

                RoleManager.setActiveRole(requireContext(), RoleManager.Role.CITIZEN);
                FirebaseDatabaseHelper.saveUserRoleInFirebase(emailStr, "CITIZEN", nameStr);

                Toast.makeText(getContext(), "✅ Account Saved to Firebase Firestore!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_signUp_to_citizenDashboard);
            });
        }

        View tvLogin = view.findViewById(R.id.tv_go_login);
        if (tvLogin != null) {
            tvLogin.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_signUp_to_login)
            );
        }

        return view;
    }
}
