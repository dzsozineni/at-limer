package com.example.atlimer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int BT_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Android 12+ permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                }, BT_PERMISSION_REQUEST);
                return;
            }
        }

        // 🔒 SEMMI BT, SEMMI heartbeat ITT
        // Csak továbblépünk
        openDashboard();
    }

    private void openDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        // ⚠️ NEM hívunk finish()-t
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == BT_PERMISSION_REQUEST) {
            if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openDashboard();
            } else {
                Toast.makeText(this,
                        "Bluetooth permission required",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
