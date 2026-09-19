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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

public class AdminHotspotDashboardFragment extends Fragment {

    private TextView tvKpiTotal;
    private TextView tvKpiCrews;
    private TextView tvKpiResolved;
    private TextView tvHeatmapTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_hotspot_dashboard, container, false);

        RoleManager.setActiveRole(requireContext(), RoleManager.Role.ADMIN);

        tvKpiTotal = view.findViewById(R.id.tv_kpi_total);
        tvKpiCrews = view.findViewById(R.id.tv_kpi_crews);
        tvKpiResolved = view.findViewById(R.id.tv_kpi_resolved);
        tvHeatmapTitle = view.findViewById(R.id.tv_heatmap_title);

        MaterialCardView cardComplaints = view.findViewById(R.id.card_admin_complaints);
        MaterialCardView cardDispatch = view.findViewById(R.id.card_admin_dispatch);
        MaterialCardView cardVehicleGps = view.findViewById(R.id.card_admin_vehicle_gps);
        MaterialCardView cardProofWork = view.findViewById(R.id.card_admin_proof_work);
        MaterialButton btnAdminLogout = view.findViewById(R.id.btn_admin_logout);

        Chip chipWardAll = view.findViewById(R.id.chip_ward_all);
        Chip chipWard14 = view.findViewById(R.id.chip_ward_14);
        Chip chipWard07 = view.findViewById(R.id.chip_ward_07);

        if (cardComplaints != null) {
            cardComplaints.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_adminComplaintManagement)
            );
        }

        if (cardDispatch != null) {
            cardDispatch.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_workerDispatch)
            );
        }

        if (cardVehicleGps != null) {
            cardVehicleGps.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_liveVehicleTrackingFragment)
            );
        }

        if (cardProofWork != null) {
            cardProofWork.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_global_proofOfWorkVerification)
            );
        }

        if (btnAdminLogout != null) {
            btnAdminLogout.setOnClickListener(v -> {
                RoleManager.clearSession(requireContext());
                Toast.makeText(getContext(), "Logged out of Municipal Admin Panel", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_global_login);
            });
        }

        if (chipWardAll != null) {
            chipWardAll.setOnClickListener(v -> updateWardStats("781", "33", "350", "🔥 [ Citywide Waste Density Heatmap ]\nHigh Density Cluster: Ward 14 Central Market"));
        }

        if (chipWard14 != null) {
            chipWard14.setOnClickListener(v -> updateWardStats("214", "12", "98", "🔥 [ Ward 14 Market Hotspot Heatmap ]\nMarket Road Cluster: Heavy Commercial Waste"));
        }

        if (chipWard07 != null) {
            chipWard07.setOnClickListener(v -> updateWardStats("142", "8", "76", "🟡 [ Ward 07 Station Depot Heatmap ]\nStation Depot Cluster: Moderate Bin Overflow"));
        }

        return view;
    }

    private void updateWardStats(String total, String crews, String resolved, String heatmapText) {
        if (tvKpiTotal != null) tvKpiTotal.setText(total);
        if (tvKpiCrews != null) tvKpiCrews.setText(crews);
        if (tvKpiResolved != null) tvKpiResolved.setText(resolved);
        if (tvHeatmapTitle != null) tvHeatmapTitle.setText(heatmapText);
    }
}
