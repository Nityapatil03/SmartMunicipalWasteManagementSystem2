package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class CitizenDashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citizen_dashboard, container, false);

        View cardReport = view.findViewById(R.id.card_action_report);
        if (cardReport != null) {
            cardReport.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_global_reportIssueFragment)
            );
        }

        View cardTrack = view.findViewById(R.id.card_action_track);
        if (cardTrack != null) {
            cardTrack.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_global_liveVehicleTrackingFragment)
            );
        }

        return view;
    }
}