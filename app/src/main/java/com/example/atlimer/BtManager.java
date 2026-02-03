package com.example.atlimer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.OutputStream;
import java.util.UUID;

public class BtManager {

    private static BluetoothSocket socket;
    private static OutputStream out;

    private static Thread heartbeatThread;
    private static boolean heartbeatRunning = false;

    private static final UUID SPP =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // ===== CONNECTION =====

    public static void connect(BluetoothDevice device) throws Exception {
        socket = device.createRfcommSocketToServiceRecord(SPP);
        socket.connect();
        out = socket.getOutputStream();
    }

    public static boolean isConnected() {
        return socket != null && socket.isConnected() && out != null;
    }

    public static void disconnect() {
        stopHeartbeat();
        try {
            if (socket != null) socket.close();
        } catch (Exception ignored) {}
        socket = null;
        out = null;
    }

    // ===== SEND =====

    public static void sendHex(String hex) {
        if (!isConnected()) return;
        try {
            out.write(hexStringToByteArray(hex));
        } catch (Exception ignored) {}
    }

    // ===== HEARTBEAT =====

    public static void startHeartbeat() {
        if (heartbeatRunning || !isConnected()) return;

        heartbeatRunning = true;
        heartbeatThread = new Thread(() -> {
            while (heartbeatRunning && isConnected()) {
                try {
                    sendHex(BtProtocol.HEARTBEAT);
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
