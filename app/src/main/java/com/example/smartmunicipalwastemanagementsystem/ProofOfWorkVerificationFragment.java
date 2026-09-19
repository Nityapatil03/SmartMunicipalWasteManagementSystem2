package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

public class ProofOfWorkVerificationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proof_of_work, container, false);

        MaterialButton btnSignoff = view.findViewById(R.id.btn_digital_signoff);
        if (btnSignoff != null) {
            btnSignoff.setOnClickListener(v -> {
                ComplaintManager.ComplaintItem complaint = ComplaintManager.getSelectedComplaint();
                if (complaint != null) {
                    ComplaintManager.updateComplaintStatus(complaint.id, ComplaintManager.Status.CLOSED, null);
                }
                Toast.makeText(getContext(), "✅ Digital Sign-off Complete! Ticket Closed.", Toast.LENGTH_LONG).show();

                if (RoleManager.isWorker(getContext())) {
                    Navigation.findNavController(v).navigate(R.id.WorkerTaskListFragment);
                } else if (RoleManager.isAdmin(getContext())) {
                    Navigation.findNavController(v).navigate(R.id.action_global_adminDashboard);
                } else {
                    Navigation.findNavController(v).navigate(R.id.MyComplaintsFragment);
                }
            });
        }

        return view;
    }
}
