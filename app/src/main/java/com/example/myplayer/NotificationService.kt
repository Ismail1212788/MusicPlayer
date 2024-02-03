package com.example.myplayer

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.system.exitProcess

class NotificationService : Service() {

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun createNotification(): Notification {
        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("You are enjoying music")
            .setSmallIcon(R.drawable.back)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Wi-Fi Scan Service"
            val descriptionText = "Scanning Wi-Fi networks"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "WifiScanService"
        private const val NOTIFICATION_ID = 101
        private const val CHANNEL_ID = "WifiScanChannel"
        private const val SCAN_INTERVAL = 3000L // 5 seconds
    }


}
