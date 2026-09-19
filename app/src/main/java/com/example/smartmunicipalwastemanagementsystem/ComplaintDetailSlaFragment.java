package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

public class ComplaintDetailSlaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint_detail_sla, container, false);

        ComplaintManager.ComplaintItem item = ComplaintManager.getSelectedComplaint();

        TextView tvId = view.findViewById(R.id.tv_detail_id);
        TextView tvTimestamp = view.findViewById(R.id.tv_detail_timestamp);
        TextView tvSlaTime = view.findViewById(R.id.tv_detail_sla_time);
        TextView tvCategory = view.findViewById(R.id.tv_detail_category);
        TextView tvLocation = view.findViewById(R.id.tv_detail_location);
        TextView tvAssignedCrew = view.findViewById(R.id.tv_detail_assigned_crew);
        TextView tvStatusBadge = view.findViewById(R.id.tv_detail_status_badge);

        MaterialButton btnTrackTruck = view.findViewById(R.id.btn_track_truck);
        MaterialButton btnEscalate = view.findViewById(R.id.btn_escalate_complaint);
        MaterialButton btnViewProof = view.findViewById(R.id.btn_view_proof);

        if (item != null) {
            if (tvId != null) tvId.setText("Complaint " + item.id + " • " + item.title);
            if (tvTimestamp != null) tvTimestamp.setText("Reported " + item.timestamp);
            if (tvSlaTime != null) tvSlaTime.setText("⏳ SLA Status: " + item.slaHoursRemaining);
            if (tvCategory != null) tvCategory.setText("Category: " + item.category);
            if (tvLocation != null) tvLocation.setText("Location: " + item.location);
            if (tvAssignedCrew != null) tvAssignedCrew.setText("2. Assigned Crew: " + item.assignedCrew + " ✅");
            if (tvStatusBadge != null) tvStatusBadge.setText("Status: " + item.status.name());
        }

        if (btnTrackTruck != null) {
            btnTrackTruck.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_liveVehicleTrackingFragment)
            );
        }

        if (btnEscalate != null) {
            btnEscalate.setOnClickListener(v -> {
                if (item != null) {
                    ComplaintManager.updateComplaintStatus(item.id, ComplaintManager.Status.ESCALATED, "Rapid Response Escalation Team");
                    if (tvStatusBadge != null) tvStatusBadge.setText("Status: ESCALATED");
                    Toast.makeText(getContext(), "⚡ Complaint Escalated to Municipal Admin Priority!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnViewProof != null) {
            btnViewProof.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_proofOfWorkVerification)
            );
        }

        return view;
    }
}
