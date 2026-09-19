package com.example.smartmunicipalwastemanagementsystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ReportIssueFragment extends Fragment {

    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<Void> takePictureLauncher;

    private ImageView ivCapturedPhoto;
    private TextView tvCameraStatus;
    private View layoutCameraPrompt;
    private Bitmap capturedBitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (bitmap != null) {
                        capturedBitmap = bitmap;
                        if (ivCapturedPhoto != null) {
                            ivCapturedPhoto.setImageBitmap(bitmap);
                            ivCapturedPhoto.setVisibility(View.VISIBLE);
                        }
                        if (layoutCameraPrompt != null) {
                            layoutCameraPrompt.setVisibility(View.GONE);
                        }
                        if (tvCameraStatus != null) {
                            tvCameraStatus.setText("Photo Captured ✅ Tap to Retake");
                        }
                        Toast.makeText(requireContext(), "Geotagged photo captured successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        takePictureLauncher.launch(null);
                    } else {
                        Toast.makeText(requireContext(), "Camera permission is required to capture photos.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_issue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View cardCamera = view.findViewById(R.id.card_camera_capture);
        ivCapturedPhoto = view.findViewById(R.id.iv_captured_photo);
        tvCameraStatus = view.findViewById(R.id.tv_camera_status);
        layoutCameraPrompt = view.findViewById(R.id.layout_camera_prompt);
        AutoCompleteTextView actvCategory = view.findViewById(R.id.actv_category);

        if (cardCamera != null) {
            cardCamera.setOnClickListener(v -> checkCameraPermissionAndCapture());
        }

        View btnSubmit = view.findViewById(R.id.btn_submit_report);
        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(v -> {
                String categoryStr = actvCategory != null && actvCategory.getText() != null
                        ? actvCategory.getText().toString()
                        : "Overflowing Community Bin";
                String locationStr = "Green Park Main Road, Ward 14";

                // Run AI Automated Complaint Validation
                ComplaintValidationEngine.ValidationResult validation =
                        ComplaintValidationEngine.validateComplaint(categoryStr, locationStr, capturedBitmap);

                if (!validation.isValid) {
                    // Send Rejection Notification
                    NotificationHelper.sendCitizenNotification(
                            requireContext(),
                            "❌ Complaint Rejected",
                            validation.rejectionReason
                    );

                    // Show Rejection Explanation Dialog
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("❌ Complaint Validation Failed (Rejected)")
                            .setMessage(validation.rejectionReason)
                            .setPositiveButton("Try Again", null)
                            .show();

                } else {
                    // Valid Complaint - Register and Notify Admin & Worker
                    int randomNum = 8000 + (int) (Math.random() * 1000);
                    String complaintId = "#CMP-2024-" + randomNum;

                    ComplaintManager.ComplaintItem newComplaint = new ComplaintManager.ComplaintItem(
                            complaintId,
                            categoryStr,
                            categoryStr,
                            "Ward 14 • Market Road (GPS Geotagged)",
                            "Just now",
                            ComplaintManager.Status.OPEN,
                            "Ward 14 Crew #402",
                            "24h 00m Remaining",
                            18.5204, 73.8567
                    );
                    ComplaintManager.addComplaint(newComplaint);

                    // Real-Time Notification to Municipal Admin
                    NotificationHelper.sendAdminNotification(
                            requireContext(),
                            "📢 New Incident Filed: Ward 14",
                            "New valid complaint (" + complaintId + "): " + categoryStr
                    );

                    // Real-Time Notification to Sanitation Worker Crew
                    NotificationHelper.sendWorkerNotification(
                            requireContext(),
                            "🚨 New Work Order Assigned: Crew #402",
                            "Task (" + complaintId + "): Clean " + categoryStr + " at Ward 14 Market Rd"
                    );

                    // Real-Time Notification to Citizen
                    NotificationHelper.sendCitizenNotification(
                            requireContext(),
                            "✅ Complaint Verified & Registered",
                            "Complaint " + complaintId + " approved and dispatched to Ward 14 Sanitation Crew!"
                    );

                    Toast.makeText(requireContext(), "✅ Complaint " + complaintId + " Verified & Submitted!", Toast.LENGTH_LONG).show();

                    try {
                        Navigation.findNavController(v).navigate(R.id.MyComplaintsFragment);
                    } catch (Exception ignored) {
                    }
                }
            });
        }
    }

    private void checkCameraPermissionAndCapture() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            takePictureLauncher.launch(null);
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Camera Permission Needed")
                    .setMessage("This app needs camera access to capture live geotagged evidence for reporting municipal waste issues.")
                    .setPositiveButton("Grant Permission", (dialog, which) ->
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA))
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
}
