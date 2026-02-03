
package com.example.atlimer

import android.bluetooth.*
import android.content.*
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = BluetoothAdapter.getDefaultAdapter()

        val scanView = findViewById<LinearLayout>(R.id.scanView)
        val dashView = findViewById<LinearLayout>(R.id.dashboardView)

        val refreshBtn = findViewById<Button>(R.id.refreshBtn)
        val listView = findViewById<ListView>(R.id.deviceList)

        val powerBtn = findViewById<Button>(R.id.powerBtn)
        val lightBtn = findViewById<Button>(R.id.lightBtn)
        val backBtn = findViewById<Button>(R.id.backBtn)

        fun showScan() {
            dashView.visibility = View.GONE
            scanView.visibility = View.VISIBLE
        }

        fun showDash() {
            scanView.visibility = View.GONE
            dashView.visibility = View.VISIBLE
        }

        fun loadDevices() {
            val devices = adapter.bondedDevices.toList()
            val names = devices.map { "${it.name} (${it.address})" }
            listView.adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, names)

            listView.setOnItemClickListener { _, _, pos, _ ->
                val dev = devices[pos]
                val intent = Intent(this, BtService::class.java)
                intent.putExtra("address", dev.address)
                startForegroundService(intent)
                showDash()
            }
        }

        refreshBtn.setOnClickListener { loadDevices() }

        powerBtn.setOnClickListener {
            sendBroadcast(Intent("POWER_TOGGLE"))
        }

        lightBtn.setOnClickListener {
            sendBroadcast(Intent("LIGHT_TOGGLE"))
        }

        backBtn.setOnClickListener {
            stopService(Intent(this, BtService::class.java))
            showScan()
        }

        loadDevices()
    }
}
