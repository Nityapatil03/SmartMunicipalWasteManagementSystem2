package com.example.smartmunicipalwastemanagementsystem;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MyComplaintsFragment extends Fragment {

    private LinearLayout container;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup containerView, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_complaints, containerView, false);

        container = view.findViewById(R.id.layout_complaints_container);
        tabLayout = view.findViewById(R.id.tab_complaint_status);

        if (tabLayout != null) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    renderComplaints(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }

        renderComplaints(0);
        return view;
    }

    private void renderComplaints(int filterTab) {
        if (container == null || getContext() == null) return;
        container.removeAllViews();

        List<ComplaintManager.ComplaintItem> allItems = ComplaintManager.getComplaints();

        for (ComplaintManager.ComplaintItem item : allItems) {
            // Filter 0 = All, 1 = Open, 2 = Escalated, 3 = Closed
            if (filterTab == 1 && item.status != ComplaintManager.Status.OPEN && item.status != ComplaintManager.Status.IN_PROGRESS) {
                continue;
            }
            if (filterTab == 2 && item.status != ComplaintManager.Status.ESCALATED) {
                continue;
            }
            if (filterTab == 3 && item.status != ComplaintManager.Status.CLOSED) {
                continue;
            }

            MaterialCardView card = new MaterialCardView(requireContext());
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 24);
            card.setLayoutParams(cardParams);
            card.setCardElevation(4);
            card.setRadius(24);
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));

            LinearLayout cardContent = new LinearLayout(requireContext());
            cardContent.setOrientation(LinearLayout.VERTICAL);
            cardContent.setPadding(32, 32, 32, 32);

            LinearLayout topRow = new LinearLayout(requireContext());
            topRow.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvTitle = new TextView(requireContext());
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tvTitle.setLayoutParams(titleParams);
            tvTitle.setText(item.id + " • " + item.title);
            tvTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
            tvTitle.setTextSize(14);
            tvTitle.setTypeface(null, Typeface.BOLD);

            TextView tvBadge = new TextView(requireContext());
            tvBadge.setPadding(20, 8, 20, 8);
            tvBadge.setTextSize(11);
            tvBadge.setTypeface(null, Typeface.BOLD);

            switch (item.status) {
                case ESCALATED:
                    tvBadge.setText("Escalated");
                    tvBadge.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_status_escalated));
                    tvBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_escalated));
                    break;
                case CLOSED:
                    tvBadge.setText("Closed");
                    tvBadge.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_status_completed));
                    tvBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_completed));
                    break;
                case IN_PROGRESS:
                    tvBadge.setText("In Progress");
                    tvBadge.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_status_in_progress));
                    tvBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_in_progress));
                    break;
                case OPEN:
                default:
                    tvBadge.setText("Open");
                    tvBadge.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_status_pending));
                    tvBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.status_pending));
                    break;
            }

            topRow.addView(tvTitle);
            topRow.addView(tvBadge);

            TextView tvDetails = new TextView(requireContext());
            LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            detailsParams.setMargins(0, 12, 0, 0);
            tvDetails.setLayoutParams(detailsParams);
            tvDetails.setText("📍 " + item.location + " • " + item.timestamp);
            tvDetails.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
            tvDetails.setTextSize(12);

            cardContent.addView(topRow);
            cardContent.addView(tvDetails);
            card.addView(cardContent);

            card.setOnClickListener(v -> {
                ComplaintManager.setSelectedComplaint(item);
                try {
                    Navigation.findNavController(v).navigate(R.id.ComplaintDetailSlaFragment);
                } catch (Exception e) {
                    // Fallback
                }
            });

            container.addView(card);
        }

        if (container.getChildCount() == 0) {
            TextView emptyTv = new TextView(requireContext());
            emptyTv.setText("No registered complaints in this category.");
            emptyTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary));
            emptyTv.setPadding(16, 32, 16, 16);
            container.addView(emptyTv);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tabLayout != null) {
            renderComplaints(tabLayout.getSelectedTabPosition());
        }
    }
}
