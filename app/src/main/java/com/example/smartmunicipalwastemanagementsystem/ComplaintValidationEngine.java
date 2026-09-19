package com.example.smartmunicipalwastemanagementsystem;

import android.graphics.Bitmap;

import java.util.List;

public class ComplaintValidationEngine {

    public static class ValidationResult {
        public final boolean isValid;
        public final String rejectionReason;
        public final int confidenceScore;

        public ValidationResult(boolean isValid, String rejectionReason, int confidenceScore) {
            this.isValid = isValid;
            this.rejectionReason = rejectionReason;
            this.confidenceScore = confidenceScore;
        }
    }

    public static ValidationResult validateComplaint(String category, String location, Bitmap photoBitmap) {
        // 1. Check Mandatory Geotagged Photo Evidence
        if (photoBitmap == null) {
            return new ValidationResult(false, "📷 Missing Photo Evidence: Live geotagged photo capture is required for AI validation.", 0);
        }

        // 2. Check Spam / Invalid Text Keywords
        if (category == null || category.trim().isEmpty()) {
            return new ValidationResult(false, "⚠️ Invalid Waste Category: Please select a valid municipal waste category.", 10);
        }

        String categoryLower = category.toLowerCase();
        if (categoryLower.contains("test") || categoryLower.contains("fake") || categoryLower.contains("spam") || categoryLower.contains("asdf")) {
            return new ValidationResult(false, "🚫 Spam Report Rejected: System AI detected non-genuine or test complaint content.", 15);
        }

        // 3. Check Duplicate Active Complaints in ComplaintManager
        List<ComplaintManager.ComplaintItem> activeComplaints = ComplaintManager.getComplaints();
        for (ComplaintManager.ComplaintItem existing : activeComplaints) {
            if (existing.status != ComplaintManager.Status.CLOSED) {
                if (existing.category.equalsIgnoreCase(category) && existing.location.equalsIgnoreCase(location)) {
                    return new ValidationResult(
                            false,
                            "🔁 Duplicate Incident Rejected: An active complaint (" + existing.id + ") is already registered for this location.",
                            30
                    );
                }
            }
        }

        // Valid Complaint
        return new ValidationResult(true, "Complaint Verified Valid ✅", 98);
    }
}
