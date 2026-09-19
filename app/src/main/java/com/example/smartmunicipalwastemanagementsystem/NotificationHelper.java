package com.example.smartmunicipalwastemanagementsystem;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static final String CHANNEL_ADMIN_ID = "channel_admin_alerts";
    public static final String CHANNEL_WORKER_ID = "channel_worker_alerts";
    public static final String CHANNEL_CITIZEN_ID = "channel_citizen_alerts";

    private static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && context != null) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                NotificationChannel adminChannel = new NotificationChannel(
                        CHANNEL_ADMIN_ID,
                        "Municipal Admin Alerts",
                        NotificationManager.IMPORTANCE_HIGH
                );
                adminChannel.setDescription("Real-time notifications for Municipal Admin Controllers");

                NotificationChannel workerChannel = new NotificationChannel(
                        CHANNEL_WORKER_ID,
                        "Sanitation Worker Task Alerts",
                        NotificationManager.IMPORTANCE_HIGH
                );
                workerChannel.setDescription("Task assignment & route dispatch alerts for Field Workers");

                NotificationChannel citizenChannel = new NotificationChannel(
                        CHANNEL_CITIZEN_ID,
                        "Citizen Complaint Updates",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                citizenChannel.setDescription("Resolution & status updates for filed complaints");

                manager.createNotificationChannel(adminChannel);
                manager.createNotificationChannel(workerChannel);
                manager.createNotificationChannel(citizenChannel);
            }
        }
    }

    public static void sendAdminNotification(Context context, String title, String message) {
        if (context == null) return;
        createNotificationChannels(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ADMIN_ID)
                .setSmallIcon(R.drawable.ic_recycle_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException ignored) {
        }
    }

    public static void sendWorkerNotification(Context context, String title, String message) {
        if (context == null) return;
        createNotificationChannels(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_WORKER_ID)
                .setSmallIcon(R.drawable.ic_truck_gps)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException ignored) {
        }
    }

    public static void sendCitizenNotification(Context context, String title, String message) {
        if (context == null) return;
        createNotificationChannels(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 2, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_CITIZEN_ID)
                .setSmallIcon(R.drawable.ic_verified_badge)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException ignored) {
        }
    }
}
