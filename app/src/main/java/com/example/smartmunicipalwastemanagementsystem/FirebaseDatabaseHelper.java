package com.example.smartmunicipalwastemanagementsystem;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelper {

    private static final String TAG = "FirebaseWasteDb";

    public static final String DATABASE_URL = "https://smwms-28685-default-rtdb.firebaseio.com";
    public static final String COLLECTION_COMPLAINTS = "complaints";
    public static final String COLLECTION_VEHICLES = "vehicles";
    public static final String COLLECTION_USERS = "users";

    private static FirebaseFirestore firestoreDb;
    private static FirebaseDatabase realtimeDb;

    public interface OnComplaintsLoadedListener {
        void onComplaintsLoaded(List<ComplaintManager.ComplaintItem> complaints);
    }

    public static synchronized FirebaseFirestore getFirestoreInstance() {
        if (firestoreDb == null) {
            try {
                firestoreDb = FirebaseFirestore.getInstance();
            } catch (Exception e) {
                Log.e(TAG, "Error initializing Firestore: " + e.getMessage());
            }
        }
        return firestoreDb;
    }

    public static synchronized FirebaseDatabase getRealtimeDbInstance() {
        if (realtimeDb == null) {
            try {
                realtimeDb = FirebaseDatabase.getInstance(DATABASE_URL);
                realtimeDb.setPersistenceEnabled(true);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing Realtime Database: " + e.getMessage());
            }
        }
        return realtimeDb;
    }

    private static String sanitizeKey(String key) {
        if (key == null) return "item_" + System.currentTimeMillis();
        return key.replace("#", "").replace(".", "_").replace("$", "").replace("[", "").replace("]", "");
    }

    public static void saveComplaintToFirebase(ComplaintManager.ComplaintItem complaint) {
        // Save to Realtime Database
        try {
            FirebaseDatabase rdb = getRealtimeDbInstance();
            if (rdb != null) {
                DatabaseReference ref = rdb.getReference(COLLECTION_COMPLAINTS).child(sanitizeKey(complaint.id));
                Map<String, Object> data = new HashMap<>();
                data.put("id", complaint.id);
                data.put("title", complaint.title);
                data.put("category", complaint.category);
                data.put("location", complaint.location);
                data.put("timestamp", complaint.timestamp);
                data.put("status", complaint.status.name());
                data.put("assignedCrew", complaint.assignedCrew);
                data.put("slaHoursRemaining", complaint.slaHoursRemaining);
                data.put("lat", complaint.lat);
                data.put("lng", complaint.lng);

                ref.setValue(data)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Realtime DB saved: " + complaint.id))
                        .addOnFailureListener(e -> Log.e(TAG, "Realtime DB error: " + e.getMessage()));
            }
        } catch (Exception e) {
            Log.e(TAG, "Realtime DB error: " + e.getMessage());
        }

        // Save to Firestore
        try {
            FirebaseFirestore firestore = getFirestoreInstance();
            if (firestore != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", complaint.id);
                data.put("title", complaint.title);
                data.put("category", complaint.category);
                data.put("location", complaint.location);
                data.put("timestamp", complaint.timestamp);
                data.put("status", complaint.status.name());
                data.put("assignedCrew", complaint.assignedCrew);
                data.put("slaHoursRemaining", complaint.slaHoursRemaining);
                data.put("lat", complaint.lat);
                data.put("lng", complaint.lng);

                firestore.collection(COLLECTION_COMPLAINTS)
                        .document(complaint.id)
                        .set(data);
            }
        } catch (Exception e) {
            Log.e(TAG, "Firestore error: " + e.getMessage());
        }
    }

    public static void updateComplaintStatusInFirebase(String complaintId, String newStatus, String assignedCrew) {
        // Realtime DB update
        try {
            FirebaseDatabase rdb = getRealtimeDbInstance();
            if (rdb != null) {
                DatabaseReference ref = rdb.getReference(COLLECTION_COMPLAINTS).child(sanitizeKey(complaintId));
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", newStatus);
                if (assignedCrew != null) {
                    updates.put("assignedCrew", assignedCrew);
                }
                ref.updateChildren(updates);
            }
        } catch (Exception e) {
            Log.e(TAG, "Realtime DB update error: " + e.getMessage());
        }

        // Firestore update
        try {
            FirebaseFirestore firestore = getFirestoreInstance();
            if (firestore != null) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", newStatus);
                if (assignedCrew != null) {
                    updates.put("assignedCrew", assignedCrew);
                }
                firestore.collection(COLLECTION_COMPLAINTS).document(complaintId).update(updates);
            }
        } catch (Exception e) {
            Log.e(TAG, "Firestore update error: " + e.getMessage());
        }
    }

    public static void listenToFirebaseComplaints(OnComplaintsLoadedListener listener) {
        // Realtime Database Listener
        try {
            FirebaseDatabase rdb = getRealtimeDbInstance();
            if (rdb != null) {
                rdb.getReference(COLLECTION_COMPLAINTS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<ComplaintManager.ComplaintItem> list = new ArrayList<>();
                            for (DataSnapshot child : snapshot.getChildren()) {
                                String id = child.child("id").getValue(String.class);
                                String title = child.child("title").getValue(String.class);
                                String category = child.child("category").getValue(String.class);
                                String location = child.child("location").getValue(String.class);
                                String timestamp = child.child("timestamp").getValue(String.class);
                                String statusStr = child.child("status").getValue(String.class);
                                String assignedCrew = child.child("assignedCrew").getValue(String.class);
                                String sla = child.child("slaHoursRemaining").getValue(String.class);
                                Double lat = child.child("lat").getValue(Double.class);
                                Double lng = child.child("lng").getValue(Double.class);

                                ComplaintManager.Status status = ComplaintManager.Status.OPEN;
                                if (statusStr != null) {
                                    try {
                                        status = ComplaintManager.Status.valueOf(statusStr);
                                    } catch (Exception ignored) {
                                    }
                                }

                                if (id != null && title != null) {
                                    list.add(new ComplaintManager.ComplaintItem(
                                            id, title,
                                            category != null ? category : "General Waste",
                                            location != null ? location : "Ward 14",
                                            timestamp != null ? timestamp : "Recent",
                                            status,
                                            assignedCrew != null ? assignedCrew : "Unassigned",
                                            sla != null ? sla : "24h SLA",
                                            lat != null ? lat : 18.5204,
                                            lng != null ? lng : 73.8567
                                    ));
                                }
                            }
                            if (listener != null) {
                                listener.onComplaintsLoaded(list);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Realtime DB cancelled: " + error.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Realtime DB listen error: " + e.getMessage());
        }

        // Firestore Listener
        try {
            FirebaseFirestore firestore = getFirestoreInstance();
            if (firestore != null) {
                firestore.collection(COLLECTION_COMPLAINTS).addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null && !value.isEmpty()) {
                        List<ComplaintManager.ComplaintItem> list = new ArrayList<>();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            String id = doc.getString("id");
                            String title = doc.getString("title");
                            String category = doc.getString("category");
                            String location = doc.getString("location");
                            String timestamp = doc.getString("timestamp");
                            String statusStr = doc.getString("status");
                            String assignedCrew = doc.getString("assignedCrew");
                            String sla = doc.getString("slaHoursRemaining");
                            Double lat = doc.getDouble("lat");
                            Double lng = doc.getDouble("lng");

                            ComplaintManager.Status status = ComplaintManager.Status.OPEN;
                            if (statusStr != null) {
                                try {
                                    status = ComplaintManager.Status.valueOf(statusStr);
                                } catch (Exception ignored) {
                                }
                            }

                            if (id != null && title != null) {
                                list.add(new ComplaintManager.ComplaintItem(
                                        id, title,
                                        category != null ? category : "General Waste",
                                        location != null ? location : "Ward 14",
                                        timestamp != null ? timestamp : "Recent",
                                        status,
                                        assignedCrew != null ? assignedCrew : "Unassigned",
                                        sla != null ? sla : "24h SLA",
                                        lat != null ? lat : 18.5204,
                                        lng != null ? lng : 73.8567
                                ));
                            }
                        }
                        if (listener != null) {
                            listener.onComplaintsLoaded(list);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Firestore listen error: " + e.getMessage());
        }
    }

    public static void updateVehicleGpsInFirebase(String vehicleId, double lat, double lng, String speed) {
        try {
            FirebaseDatabase rdb = getRealtimeDbInstance();
            if (rdb != null) {
                DatabaseReference ref = rdb.getReference(COLLECTION_VEHICLES).child(sanitizeKey(vehicleId));
                Map<String, Object> data = new HashMap<>();
                data.put("vehicleId", vehicleId);
                data.put("lat", lat);
                data.put("lng", lng);
                data.put("speed", speed);
                data.put("lastUpdated", System.currentTimeMillis());
                ref.setValue(data);
            }
        } catch (Exception e) {
            Log.e(TAG, "Realtime DB GPS update error: " + e.getMessage());
        }
    }

    public static void saveUserRoleInFirebase(String email, String role, String fullName) {
        try {
            FirebaseDatabase rdb = getRealtimeDbInstance();
            if (rdb != null && email != null) {
                DatabaseReference ref = rdb.getReference(COLLECTION_USERS).child(sanitizeKey(email));
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("email", email);
                userMap.put("role", role);
                userMap.put("fullName", fullName);
                userMap.put("registeredAt", System.currentTimeMillis());
                ref.setValue(userMap);
            }
        } catch (Exception e) {
            Log.e(TAG, "Realtime DB user save error: " + e.getMessage());
        }
    }
}
