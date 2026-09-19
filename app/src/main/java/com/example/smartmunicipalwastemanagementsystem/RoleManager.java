package com.example.smartmunicipalwastemanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;

public class RoleManager {
    public enum Role {
        NONE,
        CITIZEN,
        ADMIN,
        WORKER
    }

    private static final String PREF_NAME = "SmartWasteRolePrefs";
    private static final String KEY_USER_ROLE = "active_user_role";

    public static void setActiveRole(Context context, Role role) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USER_ROLE, role.name()).apply();
    }

    public static Role getActiveRole(Context context) {
        if (context == null) return Role.NONE;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String roleStr = prefs.getString(KEY_USER_ROLE, Role.NONE.name());
        try {
            return Role.valueOf(roleStr);
        } catch (Exception e) {
            return Role.NONE;
        }
    }

    public static boolean isCitizen(Context context) {
        return getActiveRole(context) == Role.CITIZEN;
    }

    public static boolean isAdmin(Context context) {
        return getActiveRole(context) == Role.ADMIN;
    }

    public static boolean isWorker(Context context) {
        return getActiveRole(context) == Role.WORKER;
    }

    public static void clearSession(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}