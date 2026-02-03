package com.example.atlimer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.OutputStream;
import java.util.UUID;

public class BtManager {

    static BluetoothSocket socket;
    static OutputStream out;

    static UUID SPP =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static void connect(BluetoothDevice device) {
        try {
            socket = device.createRfcommSocketToServiceRecord(SPP);
            socket.connect();
            out = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendHex(String hex) {
        try {
            byte[] data = hexStringToByteArray(hex);
            out.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
            data[i / 2] = (byte)
                    ((Character.digit(s.charAt(i), 16) << 4)
                            + Character.digit(s.charAt(i+1), 16));
        return data;
    }
}
