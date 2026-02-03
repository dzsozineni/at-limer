package com.example.atlimer;

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

        btnLock.setOnClickListener(v -> toggleLock());
        btnLight.setOnClickListener(v -> toggleLight());
    }

    void toggleLock() {
    locked = !locked;

    if (locked) {
        btnLock.setText("🔒 Locked");
        BtManager.sendHex(BtManager.POWER_OFF);

        // kis delay → lámpa OFF
        btnLock.postDelayed(() ->
                BtManager.sendHex(BtManager.LIGHT_OFF), 300);

        Toast.makeText(this, "Scooter locked", Toast.LENGTH_SHORT).show();
    } else {
        btnLock.setText("🔓 Unlock");
        BtManager.sendHex(BtManager.POWER_ON);

        // kis delay → lámpa ON
        btnLock.postDelayed(() ->
                BtManager.sendHex(BtManager.LIGHT_ON), 300);

        Toast.makeText(this, "Scooter unlocked", Toast.LENGTH_SHORT).show();
    }
}

void toggleLight() {
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
