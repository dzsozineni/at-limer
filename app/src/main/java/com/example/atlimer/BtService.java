package com.example.atlimer;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BtService extends Service {

    public static final String ACTION_START = "BT_START";
    public static final String ACTION_STOP  = "BT_STOP";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(1, NotificationFactory.create(this));

        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            BtManager.disconnect();
            stopSelf();
            return START_NOT_STICKY;
        }

        // ide később a kiválasztott BluetoothDevice jön
        // most feltételezzük, hogy már megvan

        try {
            BtManager.startHeartbeat();
        } catch (Exception ignored) {}

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        BtManager.disconnect();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
