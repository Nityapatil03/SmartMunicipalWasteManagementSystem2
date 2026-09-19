package com.example.smartmunicipalwastemanagementsystem;

import java.util.Locale;

public class MapplsConfig {
    public static final String MAPPLS_REST_API_KEY = "mappls_smart_waste_management_demo_key";

    public static String getMapplsMapHtml(double initialLat, double initialLng, String vehicleName, String vehicleNumber,
                                         String routeName, String startLocation, String destLocation, double[][] routeWaypoints) {
        StringBuilder waypointsJs = new StringBuilder("[");
        if (routeWaypoints != null && routeWaypoints.length > 0) {
            for (int i = 0; i < routeWaypoints.length; i++) {
                waypointsJs.append(String.format(Locale.US, "[%.6f, %.6f]", routeWaypoints[i][0], routeWaypoints[i][1]));
                if (i < routeWaypoints.length - 1) waypointsJs.append(",");
            }
        } else {
            waypointsJs.append(String.format(Locale.US,
                    "[%.6f, %.6f],[%.6f, %.6f],[%.6f, %.6f],[%.6f, %.6f],[%.6f, %.6f]",
                    initialLat - 0.005, initialLng - 0.007,
                    initialLat - 0.002, initialLng - 0.003,
                    initialLat, initialLng,
                    initialLat + 0.003, initialLng + 0.004,
                    initialLat + 0.006, initialLng + 0.008));
        }
        waypointsJs.append("]");

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" />\n" +
                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.css\" />\n" +
                "    <script src=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.js\"></script>\n" +
                "    <style>\n" +
                "        body, html, #map { margin: 0; padding: 0; width: 100%; height: 100%; font-family: -apple-system, sans-serif; background: #e5e3df; }\n" +
                "        .mappls-watermark { position: absolute; top: 12px; right: 12px; z-index: 1000; background: rgba(30, 86, 49, 0.95); color: #FFFFFF; padding: 6px 12px; border-radius: 20px; font-size: 11px; font-weight: bold; letter-spacing: 0.5px; box-shadow: 0 3px 8px rgba(0,0,0,0.3); }\n" +
                "        .truck-pulse-container { position: relative; width: 44px; height: 44px; display: flex; align-items: center; justify-content: center; }\n" +
                "        .pulse-ring { position: absolute; width: 44px; height: 44px; border-radius: 50%; background: rgba(46, 125, 50, 0.45); animation: pulseWave 1.8s infinite ease-out; }\n" +
                "        @keyframes pulseWave { 0% { transform: scale(0.5); opacity: 1; } 100% { transform: scale(1.6); opacity: 0; } }\n" +
                "        .truck-icon-body { position: relative; z-index: 2; background: #1E5631; border: 2.5px solid #FFFFFF; border-radius: 50%; color: white; width: 34px; height: 34px; display: flex; align-items: center; justify-content: center; font-size: 18px; box-shadow: 0 4px 10px rgba(0,0,0,0.4); }\n" +
                "        .start-marker { background: #2E7D32; border: 2px solid #FFFFFF; border-radius: 50%; color: white; padding: 4px 8px; font-size: 11px; font-weight: bold; box-shadow: 0 3px 6px rgba(0,0,0,0.3); white-space: nowrap; }\n" +
                "        .dest-marker { background: #D32F2F; border: 2px solid #FFFFFF; border-radius: 50%; color: white; padding: 4px 8px; font-size: 11px; font-weight: bold; box-shadow: 0 3px 6px rgba(0,0,0,0.3); white-space: nowrap; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"map\"></div>\n" +
                "    <div class=\"mappls-watermark\">🇮🇳 Mappls MapmyIndia Live GPS</div>\n" +
                "    <script>\n" +
                "        var map = L.map('map', { zoomControl: false }).setView([" + initialLat + ", " + initialLng + "], 15);\n" +
                "        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                "            maxZoom: 19,\n" +
                "            attribution: '© Mappls MapmyIndia'\n" +
                "        }).addTo(map);\n" +
                "        L.control.zoom({ position: 'bottomright' }).addTo(map);\n" +
                "        var routeCoords = " + waypointsJs + ";\n" +
                "        var routeGlow = L.polyline(routeCoords, { color: '#81C784', weight: 12, opacity: 0.45 }).addTo(map);\n" +
                "        var routeLine = L.polyline(routeCoords, { color: '#1E5631', weight: 6, opacity: 0.95, lineCap: 'round', lineJoin: 'round' }).addTo(map);\n" +
                "        var startPt = routeCoords[0];\n" +
                "        var startIcon = L.divIcon({\n" +
                "            className: 'start-marker',\n" +
                "            html: '🟢 START',\n" +
                "            iconSize: [60, 24],\n" +
                "            iconAnchor: [30, 12]\n" +
                "        });\n" +
                "        L.marker(startPt, { icon: startIcon }).addTo(map).bindPopup('<b>🟢 START LOCATION</b><br>" + startLocation + "');\n" +
                "        var destPt = routeCoords[routeCoords.length - 1];\n" +
                "        var destIcon = L.divIcon({\n" +
                "            className: 'dest-marker',\n" +
                "            html: '🏁 DEST',\n" +
                "            iconSize: [60, 24],\n" +
                "            iconAnchor: [30, 12]\n" +
                "        });\n" +
                "        L.marker(destPt, { icon: destIcon }).addTo(map).bindPopup('<b>🏁 DESTINATION LOCATION</b><br>" + destLocation + "');\n" +
                "        for (var i = 1; i < routeCoords.length - 1; i++) {\n" +
                "            var pt = routeCoords[i];\n" +
                "            L.circleMarker(pt, { radius: 6, fillColor: '#FF9800', color: '#FFFFFF', weight: 2, fillOpacity: 1 }).addTo(map)\n" +
                "             .bindPopup('<b>Municipal Stop #' + i + '</b><br>Ward Waste Collection Point');\n" +
                "        }\n" +
                "        var truckIcon = L.divIcon({\n" +
                "            className: 'truck-pulse-container',\n" +
                "            html: '<div class=\"pulse-ring\"></div><div class=\"truck-icon-body\">🚛</div>',\n" +
                "            iconSize: [44, 44],\n" +
                "            iconAnchor: [22, 22]\n" +
                "        });\n" +
                "        var truckMarker = L.marker([" + initialLat + ", " + initialLng + "], { icon: truckIcon }).addTo(map);\n" +
                "        truckMarker.bindPopup('<b>" + vehicleName + "</b><br>🚘 Reg No: <b>" + vehicleNumber + "</b><br>" + routeName + "').openPopup();\n" +
                "        function updateGpsPosition(lat, lng, labelText) {\n" +
                "            var newPos = new L.LatLng(lat, lng);\n" +
                "            truckMarker.setLatLng(newPos);\n" +
                "            map.panTo(newPos, { animate: true, duration: 1.2 });\n" +
                "            truckMarker.setPopupContent('<b>' + labelText + '</b><br>🚘 Reg No: <b>" + vehicleNumber + "</b><br>📍 Moving on Route: ' + lat.toFixed(4) + '° N, ' + lng.toFixed(4) + '° E');\n" +
                "        }\n" +
                "        function centerMap(lat, lng) {\n" +
                "            map.setView([lat, lng], 15, { animate: true });\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
