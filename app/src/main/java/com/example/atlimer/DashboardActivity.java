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
            Toast.makeText(this, "Scooter locked", Toast.LENGTH_SHORT).show();
        } else {
            btnLock.setText("🔓 Unlock");
            Toast.makeText(this, "Scooter unlocked", Toast.LENGTH_SHORT).show();
        }
    }

    void toggleLight() {
        lightOn = !lightOn;

        if (lightOn) {
            btnLight.setText("💡 Light OFF");
            Toast.makeText(this, "Light ON", Toast.LENGTH_SHORT).show();
        } else {
            btnLight.setText("💡 Light ON");
            Toast.makeText(this, "Light OFF", Toast.LENGTH_SHORT).show();
        }
    }
}
