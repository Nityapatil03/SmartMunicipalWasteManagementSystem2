package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class WorkerTaskListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_task_list, container, false);

        RoleManager.setActiveRole(requireContext(), RoleManager.Role.WORKER);

        MaterialCardView cardDispatchMap = view.findViewById(R.id.card_worker_dispatch_map);
        MaterialCardView cardLiveGps = view.findViewById(R.id.card_worker_live_gps);
        MaterialCardView cardProofAudit = view.findViewById(R.id.card_worker_proof_audit);

        MaterialButton btnWorkerLogout = view.findViewById(R.id.btn_worker_logout);
        SwitchMaterial switchRouteOpt = view.findViewById(R.id.switch_route_opt);

        MaterialButton btnTaskProof1 = view.findViewById(R.id.btn_task_proof_1);
        MaterialButton btnTaskComplete1 = view.findViewById(R.id.btn_task_complete_1);
        TextView tvTaskStatus1 = view.findViewById(R.id.tv_task_status_1);

        MaterialButton btnTaskProof2 = view.findViewById(R.id.btn_task_proof_2);
        MaterialButton btnTaskComplete2 = view.findViewById(R.id.btn_task_complete_2);
        TextView tvTaskStatus2 = view.findViewById(R.id.tv_task_status_2);

        if (cardDispatchMap != null) {
            cardDispatchMap.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_workerDispatch)
            );
        }

        if (cardLiveGps != null) {
            cardLiveGps.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_liveVehicleTrackingFragment)
            );
        }

        if (cardProofAudit != null) {
            cardProofAudit.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_proofOfWorkVerification)
            );
        }

        if (btnWorkerLogout != null) {
            btnWorkerLogout.setOnClickListener(v -> {
                RoleManager.clearSession(requireContext());
                Toast.makeText(getContext(), "Logged out of Sanitation Worker Portal", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_global_login);
            });
        }

        if (switchRouteOpt != null) {
            switchRouteOpt.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String msg = isChecked ? "⚡ GPS Route Optimization Enabled (Saving 22 mins)" : "Standard Route Order Active";
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            });
        }

        if (btnTaskProof1 != null) {
            btnTaskProof1.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_proofOfWorkVerification)
            );
        }

        if (btnTaskProof2 != null) {
            btnTaskProof2.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_proofOfWorkVerification)
            );
        }

        if (btnTaskComplete1 != null) {
            btnTaskComplete1.setOnClickListener(v -> {
                if (tvTaskStatus1 != null) {
                    tvTaskStatus1.setText("Completed ✅");
                    tvTaskStatus1.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_status_completed));
                    tvTaskStatus1.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_completed));
                }
                Toast.makeText(getContext(), "🎉 Task #1 Marked Completed!", Toast.LENGTH_SHORT).show();

                NotificationHelper.sendAdminNotification(
                        requireContext(),
                        "✅ Task Completed by Crew #402",
                        "Sanitation Crew #402 completed Bin #104 Overflow Cleanup at Ward 14."
                );

                NotificationHelper.sendCitizenNotification(
                        requireContext(),
                        "✅ Waste Complaint Resolved",
                        "Your registered complaint at Ward 14 Green Park Rd has been cleaned and verified by Crew #402!"
                );
            });
        }

        if (btnTaskComplete2 != null) {
            btnTaskComplete2.setOnClickListener(v -> {
                if (tvTaskStatus2 != null) {
                    tvTaskStatus2.setText("Completed ✅");
                    tvTaskStatus2.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_status_completed));
                    tvTaskStatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_completed));
                }
                Toast.makeText(getContext(), "🎉 Task #2 Marked Completed!", Toast.LENGTH_SHORT).show();

                NotificationHelper.sendAdminNotification(
                        requireContext(),
                        "✅ Task Completed by Crew #402",
                        "Sanitation Crew #402 completed Market Square Litter Cleanup."
                );
            });
        }

        return view;
    }
}
