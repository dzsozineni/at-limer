package com.example.atlimer;

import com.example.atlimer.BtManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends Activity {

    boolean locked = true;
    boolean lightOn = false;

    Button btnLock;
    Button btnLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnLock = findViewById(R.id.btnLock);
        btnLight = findViewById(R.id.btnLight);

        // Gomb események
        btnLock.setOnClickListener(v -> toggleLock());
        btnLight.setOnClickListener(v -> toggleLight());

        // 💓 Heartbeat csak akkor indul, ha van BT kapcsolat
        if (BtManager.isConnected()) {
            BtManager.startHeartbeat();
        } else {
            Toast.makeText(this,
                    "Bluetooth not connected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 💓 Heartbeat mindig leáll kilépéskor
        BtManager.stopHeartbeat();
    }

    // ===== LOCK / UNLOCK =====
    void toggleLock() {

        if (!BtManager.isConnected()) {
            Toast.makeText(this,
                    "Bluetooth not connected",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        locked = !locked;

        if (locked) {
            btnLock.setText("🔒 Locked");

            // POWER OFF
            BtManager.sendHex(BtManager.POWER_OFF);

            // kis delay → LIGHT OFF
            btnLock.postDelayed(() ->
                    BtManager.sendHex(BtManager.LIGHT_OFF), 300);

            Toast.makeText(this,
                    "Scooter locked",
                    Toast.LENGTH_SHORT).show();
        } else {
            btnLock.setText("🔓 Unlock");

            // POWER ON
            BtManager.sendHex(BtManager.POWER_ON);

            // kis delay → LIGHT ON
            btnLock.postDelayed(() ->
                    BtManager.sendHex(BtManager.LIGHT_ON), 300);

            Toast.makeText(this,
                    "Scooter unlocked",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // ===== LIGHT MANUAL TOGGLE =====
    void toggleLight() {

        if (!BtManager.isConnected()) {
            Toast.makeText(this,
                    "Bluetooth not connected",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        lightOn = !lightOn;

        if (lightOn) {
            btnLight.setText("💡 Light OFF");
            BtManager.sendHex(BtManager.LIGHT_ON);
        } else {
            btnLight.setText("💡 Light ON");
            BtManager.sendHex(BtManager.LIGHT_OFF);
        }
    }
}
