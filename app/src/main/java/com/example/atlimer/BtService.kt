
package com.example.atlimer

import android.app.*
import android.bluetooth.*
import android.content.*
import android.os.*
import androidx.core.app.NotificationCompat
import java.util.*

class BtService : Service() {

    private val HEARTBEAT = "4643110100084C494D4542494B45BE8A"
    private val POWER_ON  = "464316610001F1F28F"
    private val POWER_OFF = "464316610001F0E2AE"
    private val LIGHT_ON  = "464316120001F12B26"
    private val LIGHT_OFF = "464316120001F03B07"

    private var socket: BluetoothSocket? = null
    private var device: BluetoothDevice? = null
    private var connected = false
    private var powerOn = false

    private val handler = Handler(Looper.getMainLooper())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val addr = intent?.getStringExtra("address")
        device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addr)

        createChannel()
        startForeground(1, notification())

        registerReceiver(cmdReceiver, IntentFilter().apply {
            addAction("POWER_TOGGLE")
            addAction("LIGHT_TOGGLE")
        })

        connect()
        return START_STICKY
    }

    private fun connect() {
        Thread {
            try {
                val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                socket = device!!.createRfcommSocketToServiceRecord(uuid)
                socket!!.connect()
                connected = true
                heartbeat()
            } catch (e: Exception) {
                handler.postDelayed({ connect() }, 3000)
            }
        }.start()
    }

    private fun heartbeat() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (connected) {
                    sendHex(HEARTBEAT)
                    handler.postDelayed(this, 500)
                }
            }
        }, 500)
    }

    private val cmdReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            when (i?.action) {
                "POWER_TOGGLE" -> {
                    if (powerOn) {
                        sendHex(POWER_OFF)
                        handler.postDelayed({ sendHex(LIGHT_OFF) }, 300)
                    } else {
                        sendHex(POWER_ON)
                        handler.postDelayed({ sendHex(LIGHT_ON) }, 300)
                    }
                    powerOn = !powerOn
                }
                "LIGHT_TOGGLE" -> {
                    sendHex(if (powerOn) LIGHT_OFF else LIGHT_ON)
                }
            }
        }
    }

    private fun sendHex(hex: String) {
        val bytes = hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        socket?.outputStream?.write(bytes)
    }

    override fun onDestroy() {
        unregisterReceiver(cmdReceiver)
        socket?.close()
        connected = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null

    private fun createChannel() {
        val ch = NotificationChannel("bt", "At-Limer",
            NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
    }

    private fun notification(): Notification =
        NotificationCompat.Builder(this, "bt")
            .setContentTitle("At-Limer")
            .setContentText("Scooter connected")
            .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
            .build()
}
