package com.example.smartmunicipalwastemanagementsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Locale;

public class LiveVehicleTrackingFragment extends Fragment {

    private static class VehicleInfo {
        final String title;
        final String vehicleNumber;
        final String route;
        final String driverInfo;
        final String phone;
        final String speedDistance;
        final String currentStop;
        final String startLocation;
        final String destLocation;

        VehicleInfo(String title, String vehicleNumber, String route, String driverInfo, String phone,
                    String speedDistance, String currentStop, String startLocation, String destLocation) {
            this.title = title;
            this.vehicleNumber = vehicleNumber;
            this.route = route;
            this.driverInfo = driverInfo;
            this.phone = phone;
            this.speedDistance = speedDistance;
            this.currentStop = currentStop;
            this.startLocation = startLocation;
            this.destLocation = destLocation;
        }
    }

    private final VehicleInfo[] vehicles = new VehicleInfo[]{
            new VehicleInfo("Municipal Garbage Truck #402",
                    "MH-12-GW-4021",
                    "Route: Ward 14 • Central Market → Green Park",
                    "Driver: Rajesh Kumar",
                    "9876543210",
                    "24 km/h • 2.4 km left",
                    "Current: Central Market Gate 2",
                    "Central Sanitation Depot - Ward 14",
                    "Green Park Processing Plant"),
            new VehicleInfo("Compactor Truck #108",
                    "MH-12-GW-1089",
                    "Route: Ward 07 • Suburb Depot → City Mall",
                    "Driver: Amit Sharma",
                    "9876543211",
                    "18 km/h • 1.1 km left",
                    "Current: Station Road Crossing",
                    "Suburb Rail Depot - Ward 07",
                    "City Mall Processing Center"),
            new VehicleInfo("Recycling Van #205",
                    "MH-12-GW-2055",
                    "Route: Ward 02 • Eco Hub → Sunset Colony",
                    "Driver: Suresh Patel",
                    "9876543212",
                    "32 km/h • 4.8 km left",
                    "Current: Community Center",
                    "Eco Hub Recycling Yard - Ward 02",
                    "Sunset Colony Dump Plant")
    };

    private final double[][][] vehicleRoutes = new double[][][]{
            // Vehicle 0: Truck #402 (Ward 14)
            {
                    {18.5154, 73.8497},
                    {18.5174, 73.8527},
                    {18.5194, 73.8547},
                    {18.5204, 73.8567},
                    {18.5224, 73.8597},
                    {18.5244, 73.8627},
                    {18.5264, 73.8657}
            },
            // Vehicle 1: Compactor #108 (Ward 07)
            {
                    {18.5252, 73.8351},
                    {18.5272, 73.8371},
                    {18.5292, 73.8391},
                    {18.5312, 73.8421},
                    {18.5332, 73.8451},
                    {18.5352, 73.8481},
                    {18.5372, 73.8511}
            },
            // Vehicle 2: Van #205 (Ward 02)
            {
                    {18.5080, 73.8610},
                    {18.5100, 73.8630},
                    {18.5120, 73.8660},
                    {18.5140, 73.8690},
                    {18.5160, 73.8720},
                    {18.5180, 73.8750},
                    {18.5200, 73.8780}
            }
    };

    private int currentVehicleIndex = 0;
    private int routeStepIndex = 3;
    private boolean movingForward = true;
    private boolean isSimulating = true;
    private int progressPercent = 50;
    private double currentLat = 18.5204;
    private double currentLng = 73.8567;

    private TextView tvVehicleTitle;
    private TextView tvVehicleNumber;
    private TextView tvVehicleRoute;
    private TextView tvLiveBadge;
    private TextView tvDriverInfo;
    private TextView tvSpeedDistance;
    private TextView tvStartLocation;
    private TextView tvDestinationLocation;
    private TextView tvGpsCoords;
    private TextView tvEtaTime;
    private TextView tvCurrentStop;
    private TextView tvProgressPercent;
    private LinearProgressIndicator progressRoute;
    private WebView webViewMapplsMap;
    private MaterialButton btnToggleLiveGps;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Runnable liveGpsRunnable = new Runnable() {
        @Override
        public void run() {
            if (isSimulating && isAdded()) {
                updateLiveGpsSimulation();
            }
            mainHandler.postDelayed(this, 2000);
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_vehicle_tracking, container, false);

        Chip chipTruck402 = view.findViewById(R.id.chip_truck_402);
        Chip chipTruck108 = view.findViewById(R.id.chip_truck_108);
        Chip chipTruck205 = view.findViewById(R.id.chip_truck_205);

        tvVehicleTitle = view.findViewById(R.id.tv_vehicle_title);
        tvVehicleNumber = view.findViewById(R.id.tv_vehicle_number);
        tvVehicleRoute = view.findViewById(R.id.tv_vehicle_route);
        tvLiveBadge = view.findViewById(R.id.tv_live_badge);
        tvDriverInfo = view.findViewById(R.id.tv_driver_info);
        tvSpeedDistance = view.findViewById(R.id.tv_speed_distance);
        tvStartLocation = view.findViewById(R.id.tv_start_location);
        tvDestinationLocation = view.findViewById(R.id.tv_destination_location);
        tvGpsCoords = view.findViewById(R.id.tv_gps_coords);
        tvEtaTime = view.findViewById(R.id.tv_eta_time);
        tvCurrentStop = view.findViewById(R.id.tv_current_stop);
        tvProgressPercent = view.findViewById(R.id.tv_progress_percent);
        progressRoute = view.findViewById(R.id.progress_route);
        webViewMapplsMap = view.findViewById(R.id.web_view_mappls_map);

        MaterialButton btnCallDriver = view.findViewById(R.id.btn_call_driver);
        MaterialButton btnRecenterMap = view.findViewById(R.id.btn_recenter_map);
        btnToggleLiveGps = view.findViewById(R.id.btn_toggle_live_gps);

        if (webViewMapplsMap != null) {
            WebSettings settings = webViewMapplsMap.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            webViewMapplsMap.setWebViewClient(new WebViewClient());
        }

        if (chipTruck402 != null) chipTruck402.setOnClickListener(v -> selectVehicle(0));
        if (chipTruck108 != null) chipTruck108.setOnClickListener(v -> selectVehicle(1));
        if (chipTruck205 != null) chipTruck205.setOnClickListener(v -> selectVehicle(2));

        if (btnCallDriver != null) {
            btnCallDriver.setOnClickListener(v -> callDriver());
        }

        if (btnRecenterMap != null) {
            btnRecenterMap.setOnClickListener(v -> recenterMap());
        }

        if (btnToggleLiveGps != null) {
            btnToggleLiveGps.setOnClickListener(v -> toggleGpsSimulation());
        }

        selectVehicle(0);
        return view;
    }

    private void selectVehicle(int index) {
        currentVehicleIndex = index;
        VehicleInfo info = vehicles[index];
        routeStepIndex = 3;
        movingForward = true;
        currentLat = vehicleRoutes[index][routeStepIndex][0];
        currentLng = vehicleRoutes[index][routeStepIndex][1];

        if (tvVehicleTitle != null) tvVehicleTitle.setText(info.title);
        if (tvVehicleNumber != null) tvVehicleNumber.setText("🚘 Vehicle No: " + info.vehicleNumber);
        if (tvVehicleRoute != null) tvVehicleRoute.setText(info.route);
        if (tvDriverInfo != null) tvDriverInfo.setText(info.driverInfo + " • " + info.vehicleNumber);
        if (tvSpeedDistance != null) tvSpeedDistance.setText(info.speedDistance);
        if (tvCurrentStop != null) tvCurrentStop.setText(info.currentStop);
        if (tvStartLocation != null) tvStartLocation.setText("🟢 Start: " + info.startLocation);
        if (tvDestinationLocation != null) tvDestinationLocation.setText("🏁 Dest: " + info.destLocation);

        progressPercent = (int) (((double) routeStepIndex / (vehicleRoutes[index].length - 1)) * 100);

        if (webViewMapplsMap != null) {
            String htmlData = MapplsConfig.getMapplsMapHtml(
                    currentLat, currentLng, info.title, info.vehicleNumber, info.route,
                    info.startLocation, info.destLocation, vehicleRoutes[index]
            );
            webViewMapplsMap.loadDataWithBaseURL("https://maps.mappls.com/", htmlData, "text/html", "UTF-8", null);
        }

        updateUIFields();
        Toast.makeText(getContext(), "Tracking " + info.title + " (" + info.vehicleNumber + ") on Mappls Map", Toast.LENGTH_SHORT).show();
    }

    private void updateLiveGpsSimulation() {
        double[][] route = vehicleRoutes[currentVehicleIndex];
        if (movingForward) {
            routeStepIndex++;
            if (routeStepIndex >= route.length) {
                routeStepIndex = route.length - 2;
                movingForward = false;
            }
        } else {
            routeStepIndex--;
            if (routeStepIndex < 0) {
                routeStepIndex = 1;
                movingForward = true;
            }
        }

        currentLat = route[routeStepIndex][0];
        currentLng = route[routeStepIndex][1];
        progressPercent = (int) (((double) routeStepIndex / (route.length - 1)) * 100);

        if (webViewMapplsMap != null) {
            VehicleInfo info = vehicles[currentVehicleIndex];
            String jsCode = String.format(Locale.US,
                    "javascript:if(typeof updateGpsPosition==='function'){updateGpsPosition(%.6f, %.6f, '%s');}",
                    currentLat, currentLng, info.title + " (" + info.vehicleNumber + ")");
            webViewMapplsMap.evaluateJavascript(jsCode, null);
        }

        // Sync with Firebase Realtime DB
        try {
            FirebaseDatabaseHelper.updateVehicleGpsInFirebase(
                    "truck_" + (currentVehicleIndex == 0 ? "402" : (currentVehicleIndex == 1 ? "108" : "205")),
                    currentLat, currentLng, vehicles[currentVehicleIndex].speedDistance
            );
        } catch (Exception ignored) {
        }

        updateUIFields();
    }

    private void updateUIFields() {
        int eta = Math.max(1, 28 - (progressPercent * 24 / 100));

        if (tvEtaTime != null) {
            tvEtaTime.setText(eta + " Mins Away");
        }

        if (progressRoute != null) {
            progressRoute.setProgress(progressPercent);
        }

        if (tvProgressPercent != null) {
            tvProgressPercent.setText(progressPercent + "% Route Complete");
        }

        if (tvGpsCoords != null) {
            tvGpsCoords.setText(String.format(Locale.US, "GPS: %.4f° N, %.4f° E", currentLat, currentLng));
        }
    }

    private void callDriver() {
        VehicleInfo info = vehicles[currentVehicleIndex];
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + info.phone));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Calling " + info.driverInfo + " (" + info.phone + ")", Toast.LENGTH_LONG).show();
        }
    }

    private void recenterMap() {
        VehicleInfo info = vehicles[currentVehicleIndex];
        double[][] route = vehicleRoutes[currentVehicleIndex];
        currentLat = route[routeStepIndex][0];
        currentLng = route[routeStepIndex][1];

        if (webViewMapplsMap != null) {
            String jsCode = String.format(Locale.US,
                    "javascript:if(typeof centerMap==='function'){centerMap(%.6f, %.6f);}",
                    currentLat, currentLng);
            webViewMapplsMap.evaluateJavascript(jsCode, null);
        }
        updateUIFields();
        Toast.makeText(getContext(), "📍 Mappls Map recentered on " + info.title + " (" + info.vehicleNumber + ")", Toast.LENGTH_SHORT).show();
    }

    private void toggleGpsSimulation() {
        isSimulating = !isSimulating;
        if (btnToggleLiveGps != null) {
            btnToggleLiveGps.setText(isSimulating ? "Pause GPS" : "Resume GPS");
        }
        if (tvLiveBadge != null) {
            tvLiveBadge.setText(isSimulating ? "● LIVE GPS" : "PAUSED");
        }
        Toast.makeText(getContext(), isSimulating ? "Mappls Live GPS resumed" : "Mappls Live GPS paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainHandler.post(liveGpsRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        mainHandler.removeCallbacks(liveGpsRunnable);
    }
}
