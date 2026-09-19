package com.example.smartmunicipalwastemanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class ComplaintManager {

    public enum Status {
        OPEN,
        IN_PROGRESS,
        ESCALATED,
        CLOSED
    }

    public static class ComplaintItem {
        public final String id;
        public final String title;
        public final String category;
        public final String location;
        public final String timestamp;
        public Status status;
        public String assignedCrew;
        public final String slaHoursRemaining;
        public final double lat;
        public final double lng;

        public ComplaintItem(String id, String title, String category, String location,
                             String timestamp, Status status, String assignedCrew,
                             String slaHoursRemaining, double lat, double lng) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.location = location;
            this.timestamp = timestamp;
            this.status = status;
            this.assignedCrew = assignedCrew;
            this.slaHoursRemaining = slaHoursRemaining;
            this.lat = lat;
            this.lng = lng;
        }
    }

    private static final List<ComplaintItem> complaints = new ArrayList<>();
    private static ComplaintItem selectedComplaint = null;

    static {
        complaints.add(new ComplaintItem(
                "#CMP-2024-8901",
                "Uncollected Bin Overflow",
                "Overflowing Public Bin",
                "Green Park Main Road, Ward 14",
                "Just now",
                Status.OPEN,
                "Ward 14 Crew #402",
                "18h 42m Remaining",
                18.5204, 73.8567
        ));

        complaints.add(new ComplaintItem(
                "#CMP-2024-7623",
                "Commercial Waste Dumping",
                "Illegal Commercial Dumping",
                "Sector 4 Market Area, Ward 14",
                "3 hours ago",
                Status.ESCALATED,
                "Rapid Response Team",
                "SLA Exceeded (+2h)",
                18.5244, 73.8627
        ));

        complaints.add(new ComplaintItem(
                "#CMP-2024-5112",
                "Street Waste Spill Cleanup",
                "Street Litter & Spill",
                "Market Square Crossing, Ward 07",
                "Yesterday",
                Status.CLOSED,
                "Ward 07 Compactor #108",
                "Resolved in 4h 15m",
                18.5312, 73.8421
        ));

        // Start Real-time Firebase Firestore Sync
        try {
            FirebaseDatabaseHelper.listenToFirebaseComplaints(firebaseList -> {
                if (firebaseList != null && !firebaseList.isEmpty()) {
                    for (ComplaintItem item : firebaseList) {
                        boolean exists = false;
                        for (int i = 0; i < complaints.size(); i++) {
                            if (complaints.get(i).id.equalsIgnoreCase(item.id)) {
                                complaints.set(i, item);
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            complaints.add(0, item);
                        }
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

    public static List<ComplaintItem> getComplaints() {
        return new ArrayList<>(complaints);
    }

    public static void addComplaint(ComplaintItem item) {
        complaints.add(0, item);
        selectedComplaint = item;
        FirebaseDatabaseHelper.saveComplaintToFirebase(item);
    }

    public static ComplaintItem getSelectedComplaint() {
        if (selectedComplaint == null && !complaints.isEmpty()) {
            return complaints.get(0);
        }
        return selectedComplaint;
    }

    public static void setSelectedComplaint(ComplaintItem complaint) {
        selectedComplaint = complaint;
    }

    public static void updateComplaintStatus(String id, Status newStatus, String crew) {
        for (ComplaintItem c : complaints) {
            if (c.id.equalsIgnoreCase(id)) {
                c.status = newStatus;
                if (crew != null) {
                    c.assignedCrew = crew;
                }
                break;
            }
        }
        FirebaseDatabaseHelper.updateComplaintStatusInFirebase(id, newStatus.name(), crew);
    }
}
