package com.example.atlimer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.OutputStream;
import java.util.UUID;

public class BtManager {

    // ===== HEX COMMANDS =====

    // POWER
    public static final String POWER_ON  = "464316610001F1F28F";
    public static final String POWER_OFF = "464316610001F0E2AE";

    // LIGHT
    public static final String LIGHT_ON  = "464316120001F12B26";
    public static final String LIGHT_OFF = "464316120001F03B07";

    // HEARTBEAT
    public static final String HEARTBEAT =
            "4643110100084C494D4542494B45BE8A";

    // ========================

    private static BluetoothSocket socket;
    private static OutputStream out;

    private static Thread heartbeatThread;
    private static boolean heartbeatRunning = false;

    private static final UUID SPP =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // ===== CONNECTION =====

    public static void connect(BluetoothDevice device) {
        try {
            socket = device.createRfcommSocketToServiceRecord(SPP);
            socket.connect();
            out = socket.getOutputStream();
        } catch (Exception e) {
            socket = null;
            out = null;
            e.printStackTrace();
        }
    }

    public static boolean isConnected() {
        return socket != null && socket.isConnected() && out != null;
    }

    // ===== SEND =====

    public static void sendHex(String hex) {
        try {
            if (!isConnected()) return;
            byte[] data = hexStringToByteArray(hex);
            out.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== HEARTBEAT =====

    public static void startHeartbeat() {
        if (heartbeatRunning || !isConnected()) return;

        heartbeatRunning = true;
        heartbeatThread = new Thread(() -> {
            while (heartbeatRunning && isConnected()) {
                try {
                    sendHex(HEARTBEAT);
                    Thread.sleep(500);
                } catch (Exception e) {
                    heartbeatRunning = false;
                }
            }
        });
        heartbeatThread.start();
    }

    public static void stopHeartbeat() {
        heartbeatRunning = false;
    }

    // ===== UTILS =====

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)
                    ((Character.digit(s.charAt(i), 16) << 4)
                            + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
