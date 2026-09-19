package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AdminComplaintManagementFragment extends Fragment {

    private static final String[] CREWS = new String[]{
            "🚛 Crew #14: Rajesh Kumar (Ward 14 Market)",
            "🚛 Crew #07: Amit Sharma (Ward 07 Suburb)",
            "🚛 Crew #02: Suresh Patel (Ward 02 Eco Hub)",
            "⚡ Escalated Rapid Response Team"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_complaint_management, container, false);

        RoleManager.setActiveRole(requireContext(), RoleManager.Role.ADMIN);

        MaterialButton btnBulkAssign = view.findViewById(R.id.btn_bulk_assign);
        if (btnBulkAssign != null) {
            btnBulkAssign.setOnClickListener(v -> showBulkAssignDialog());
        }

        return view;
    }

    private void showBulkAssignDialog() {
        if (getContext() == null) return;

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Bulk Assign 3 Complaints")
                .setItems(CREWS, (dialog, which) -> {
                    String assignedCrew = CREWS[which];
                    Toast.makeText(getContext(), "✅ Assigned 3 complaints to " + assignedCrew, Toast.LENGTH_LONG).show();

                    // Real-Time Notification to Worker Crew
                    NotificationHelper.sendWorkerNotification(
                            requireContext(),
                            "🚨 New Batch Assignment: " + assignedCrew,
                            "Municipal Admin dispatched 3 batch complaints for immediate cleanup."
                    );

                    // Real-Time Notification to Admin Confirmation
                    NotificationHelper.sendAdminNotification(
                            requireContext(),
                            "📊 Batch Dispatch Successful",
                            "Dispatched 3 active complaints to " + assignedCrew
                    );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
