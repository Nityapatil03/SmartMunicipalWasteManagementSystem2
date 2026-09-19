# ♻️ Smart Municipal Waste Management System

> An AI-Validated, Real-time IoT & Mappls GPS Integrated Municipal Waste Management Platform for Android.

![Android](https://img.shields.io/badge/Android-SDK%2037-2E7D32?style=for-the-badge&logo=android)
![Java](https://img.shields.io/badge/Language-Java%2011-1E5631?style=for-the-badge&logo=openjdk)
![Firebase](https://img.shields.io/badge/Database-Firebase%20Firestore%20%26%20RealtimeDB-FFCA28?style=for-the-badge&logo=firebase)
![Mappls](https://img.shields.io/badge/Maps-Mappls%20MapmyIndia-00897B?style=for-the-badge&logo=googlemaps)
![Material3](https://img.shields.io/badge/UI-Material%20Design%203-4CAF50?style=for-the-badge&logo=materialdesign)

---

## 📌 Short Repository Description (Copy for GitHub Details)

```text
Android-based Smart Municipal Waste Management System featuring Mappls (MapmyIndia) live GPS fleet tracking, Firebase Realtime DB & Firestore, AI complaint validation, ward hotspot heatmaps, and real-time push notifications for citizens, municipal admins, and sanitation workers.
```

---

## 🚀 Key System Features

### 1. 📸 AI Geotagged Incident Reporting
- Mandatory live camera geotagged photo capture.
- **Automated AI Complaint Validation Engine**: Evaluates photo evidence, checks for duplicate incidents at the same location, and detects spam/invalid reports before registration.
- Automatic rejection notifications for invalid/duplicate reports with detailed reasoning.

### 2. 🗺️ Mappls (MapmyIndia) Live GPS Fleet Tracking
- Real-time Mappls vector tile maps showing active municipal garbage trucks, compactors, and recycling vans.
- **Animated GPS Movement**: Vehicle markers drive smoothly along ward collection routes with pulsating radar wave indicators.
- **Start & Destination Route Pins**: Prominently highlights **Origin Depot** and **Destination Processing Plant** with registration numbers (`MH-12-GW-4021`).
- Fleet controls: Call Driver, Recenter Map, and Pause/Resume GPS movement.

### 3. 🏛️ Municipal Corporation Admin Panel
- **Operations Control Center**: Dedicated dashboard linking Complaints SLA, Worker Dispatch, Mappls GPS, and Proof-of-Work Audits.
- **Ward Hotspot Heatmaps**: Real-time waste density analytics (Critical, Moderate, Cleared) with interactive ward filter chips.
- **Batch Complaint Dispatch**: Allows admins to bulk-assign open complaints to sanitation crews.

### 4. 👷 Field Sanitation Worker App
- **Turn-by-Turn GPS Route Optimization**: Toggleable shortest-route optimization saving fuel and time.
- **Task Order Management**: Prioritized work orders with live status updates (Urgent, In Progress, Completed).
- **Proof-of-Work Verification**: Side-by-side Before/After photo evidence audit and digital sign-off.

### 5. 🔥 Firebase Real-time Database & Notifications
- **Dual Database Architecture**: Firebase Cloud Firestore and Realtime Database (`smwms-28685-default-rtdb.firebaseio.com`) with offline persistence.
- **Real-Time Push Notifications**: Instant heads-up alerts dispatched across 3 channels:
  - **Admin Alerts**: `"📢 New Valid Incident Filed in Ward 14"`
  - **Worker Alerts**: `"🚨 New Work Order Assigned to Crew #402"`
  - **Citizen Updates**: `"✅ Complaint Verified & Dispatched"`

---

## 📱 Role-Based Portals

| Role | Core Capabilities |
| :--- | :--- |
| **🏛️ Municipal Admin** | Hotspot Heatmaps, Batch Dispatch, SLA Overviews, Worker Crew Assignment, Fleet GPS Monitoring |
| **👷 Sanitation Worker** | Task Orders, GPS Route Optimization, Proof-of-Work Photo Audits, Digital Ticket Sign-Off |
| **👤 Citizen** | Geotagged Incident Reporting, Live Truck GPS Tracking, SLA Countdown, Rewards & Status Updates |

---

## 🛠️ Tech Stack & Architecture

- **Language**: Java 11
- **UI Framework**: Android Jetpack, Material Design 3, ViewBinding
- **Navigation**: Jetpack Navigation Component with single-activity architecture
- **Database**: Firebase Realtime Database & Cloud Firestore
- **Mapping & Location**: Mappls (MapmyIndia) Vector Tiles & Leaflet Engine
- **Notifications**: Android `NotificationCompat` & Notification Channels
- **Build System**: Gradle 9.4 + AGP 8.x

---

## 📦 Installation & Setup Guide

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17
- Android SDK 37 (Min SDK 24)

### Steps to Run
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/SmartMunicipalWasteManagementSystem.git
   cd SmartMunicipalWasteManagementSystem
   ```

2. **Open in Android Studio**:
   - Open Android Studio and choose **Open an existing project**.
   - Select the project directory and wait for Gradle Sync to complete.

3. **Build & Run**:
   - Connect an Android device or start an emulator.
   - Click **Run `app`** or execute via terminal:
     ```bash
     ./gradlew :app:assembleDebug
     ```

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---
*Cleaner Today • Greener Tomorrow*
