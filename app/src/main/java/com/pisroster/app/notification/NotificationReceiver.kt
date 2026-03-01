package com.pisroster.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pisroster.app.MainActivity
import com.pisroster.app.R

class NotificationReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "PIS ROSTER"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: ""
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        
        showNotification(context, notificationId, title, message)
    }
    
    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        const val CHANNEL_ID = "pis_roster_channel"
        const val CHANNEL_NAME = "PIS ROSTER Notifications"
        
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "School management notifications"
                }
                
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }
        
        fun showNotification(context: Context, id: Int, title: String, message: String) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.notify(id, notification)
        }
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule notifications after device reboot
            // TODO: Implement rescheduling
        }
    }
}
