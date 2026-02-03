package com.example.atlimer
import android.app.Service
import android.content.Intent
import android.os.IBinder
class BtService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
