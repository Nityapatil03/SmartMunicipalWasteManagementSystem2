package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class GridShowcaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_showcase, container, false);

        setupCardClick(view, R.id.card_showcase_1, R.id.SplashFragment);
        setupCardClick(view, R.id.card_showcase_2, R.id.LoginFragment);
        setupCardClick(view, R.id.card_showcase_3, R.id.SignUpFragment);
        setupCardClick(view, R.id.card_showcase_4, R.id.ForgotPasswordFragment);
        setupCardClick(view, R.id.card_showcase_5, R.id.CitizenDashboardFragment);
        setupCardClick(view, R.id.card_showcase_6, R.id.ReportIssueFragment);
        setupCardClick(view, R.id.card_showcase_7, R.id.MyComplaintsFragment);
        setupCardClick(view, R.id.card_showcase_8, R.id.CitizenProfileRewardsFragment);
        setupCardClick(view, R.id.card_showcase_9, R.id.LiveVehicleTrackingFragment);
        setupCardClick(view, R.id.card_showcase_10, R.id.ComplaintDetailSlaFragment);
        setupCardClick(view, R.id.card_showcase_11, R.id.AdminHotspotDashboardFragment);
        setupCardClick(view, R.id.card_showcase_12, R.id.AdminHotspotDashboardFragment);
        setupCardClick(view, R.id.card_showcase_13, R.id.AdminComplaintManagementFragment);
        setupCardClick(view, R.id.card_showcase_14, R.id.WorkerDispatchFragment);
        setupCardClick(view, R.id.card_showcase_15, R.id.WorkerDispatchFragment);
        setupCardClick(view, R.id.card_showcase_16, R.id.WorkerDispatchFragment);
        setupCardClick(view, R.id.card_showcase_17, R.id.AdminHotspotDashboardFragment);
        setupCardClick(view, R.id.card_showcase_18, R.id.WorkerTaskListFragment);
        setupCardClick(view, R.id.card_showcase_19, R.id.WorkerTaskListFragment);
        setupCardClick(view, R.id.card_showcase_20, R.id.ProofOfWorkVerificationFragment);
        setupCardClick(view, R.id.card_showcase_21, R.id.WorkerTaskListFragment);
        setupCardClick(view, R.id.card_showcase_22, R.id.WorkerTaskListFragment);
        setupCardClick(view, R.id.card_showcase_23, R.id.WorkerTaskListFragment);

        return view;
    }

    private void setupCardClick(View parent, int cardId, int destinationId) {
        View card = parent.findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> Navigation.findNavController(v).navigate(destinationId));
        }
    }
}