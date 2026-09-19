package com.example.smartmunicipalwastemanagementsystem;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.smartmunicipalwastemanagementsystem.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    private static final String[] SCREENS = new String[]{
            "0. 🖼️ Master 2-Row Grid Showcase",
            "1. 🌊 Splash Screen",
            "2. 🔐 Login Screen",
            "3. 📝 Sign Up Screen",
            "4. 🔑 Forgot Password Screen",
            "5. 🏛️ Citizen Dashboard",
            "6. 🚛 Live Vehicle Tracking",
            "7. 📋 My Complaints Screen",
            "8. 🏅 Citizen Profile & Rewards",
            "9. ⏱️ Complaint Detail & SLA Tracker",
            "10. 📸 Report Incident Screen",
            "11. 🔥 Admin Hotspot Dashboard",
            "12. 📊 Admin Complaint Management",
            "13. 📍 Worker Dispatch Map",
            "14. 🚚 Worker Task List",
            "15. ✅ Proof-of-Work Verification"
    };

    private static final int[] DESTINATIONS = new int[]{
            R.id.GridShowcaseFragment,
            R.id.SplashFragment,
            R.id.LoginFragment,
            R.id.SignUpFragment,
            R.id.ForgotPasswordFragment,
            R.id.CitizenDashboardFragment,
            R.id.LiveVehicleTrackingFragment,
            R.id.MyComplaintsFragment,
            R.id.CitizenProfileRewardsFragment,
            R.id.ComplaintDetailSlaFragment,
            R.id.ReportIssueFragment,
            R.id.AdminHotspotDashboardFragment,
            R.id.AdminComplaintManagementFragment,
            R.id.WorkerDispatchFragment,
            R.id.WorkerTaskListFragment,
            R.id.ProofOfWorkVerificationFragment
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setSupportActionBar(binding.toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }

        binding.fab.setOnClickListener(view -> showScreenPickerDialog());
    }

    private void showScreenPickerDialog() {
        if (navController == null) return;

        new MaterialAlertDialogBuilder(this)
                .setTitle("Jump to Screen (15 Showcase Screens)")
                .setItems(SCREENS, (dialog, which) -> {
                    if (which >= 0 && which < DESTINATIONS.length) {
                        navController.navigate(DESTINATIONS[which]);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 5, 0, "📱 Quick Screen Switcher")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 1, 1, "Master Grid Showcase");
        menu.add(0, 2, 2, "Citizen Role");
        menu.add(0, 3, 3, "Admin Role");
        menu.add(0, 4, 4, "Worker Role");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (navController == null) return super.onOptionsItemSelected(item);

        int itemId = item.getItemId();
        if (itemId == 5) {
            showScreenPickerDialog();
            return true;
        } else if (itemId == 1) {
            navController.navigate(R.id.GridShowcaseFragment);
            return true;
        } else if (itemId == 2) {
            navController.navigate(R.id.CitizenDashboardFragment);
            return true;
        } else if (itemId == 3) {
            navController.navigate(R.id.AdminHotspotDashboardFragment);
            return true;
        } else if (itemId == 4) {
            navController.navigate(R.id.WorkerTaskListFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        boolean handled = false;
        if (navHostFragment != null) {
            NavController nc = navHostFragment.getNavController();
            int currentDestId = nc.getCurrentDestination() != null ? nc.getCurrentDestination().getId() : 0;
            if (RoleManager.isAdmin(this) && currentDestId == R.id.AdminHotspotDashboardFragment) {
                return true;
            }
            if (RoleManager.isWorker(this) && currentDestId == R.id.WorkerTaskListFragment) {
                return true;
            }
            handled = NavigationUI.navigateUp(nc, appBarConfiguration);
        }
        return handled || super.onSupportNavigateUp();
    }
}