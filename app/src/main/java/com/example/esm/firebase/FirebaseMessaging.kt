package com.example.esm.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.esm.R
import com.example.esm.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

open class FirebaseMessaging : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.v("newToken", "LatestFirebaseMessagingService Token $token")
//  getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply()

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.v("remoteMessage", "remoteMessage")
        /*if (remoteMessage.notification != null) {
            Log.v("remoteMessage", "notification!!.title "+remoteMessage.notification!!.title)
            Log.v("remoteMessage", "remoteMessage.notification "+ remoteMessage.notification!!.body)
            createNotification(
                remoteMessage.notification!!.title, remoteMessage.notification!!
                    .body, "", "", "")
        } else*/ if (remoteMessage.data .isNotEmpty()) {
            val gson = Gson()
            if (gson.toJson(remoteMessage.data) != null) {
                Log.v("remoteMessage", "remoteMessage.data "+remoteMessage.data)
                Log.v("remoteMessage", "remoteMessage.buttonType "+ remoteMessage.data["buttonType"])
                createNotification(
                    remoteMessage.data["title"],
                    remoteMessage.data["body"],
                    remoteMessage.data["buttonType"],
                    remoteMessage.data["studentId"],
                    remoteMessage.data["position"]
                )
            }
        }
        super.onMessageReceived(remoteMessage)

    }

    fun createNotification(
        title: String?,
        Message: String?,
        buttonType: String?,
        studentId: String?,
        position: String?
    ) {
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val context = applicationContext
        val notificationId = 111
        val channelId = "channel-01"
        val channelName = "Channel Name"
        var importance = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val mChannel = NotificationChannel(channelId, channelName, importance)
            mChannel.setSound(alarmSound, att)
            mChannel.vibrationPattern = longArrayOf(1000, 100, 1000)
            mChannel.enableVibration(true)
            notificationManager.createNotificationChannel(mChannel)
        }
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(Message) // .setLargeIcon(icon)
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setPriority(Notification.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000, 100, 1000))
        val notificationIntent = Intent(context, SplashActivity::class.java)
        notificationIntent.putExtra("buttonType", buttonType)
        notificationIntent.putExtra("studentId", studentId)
        notificationIntent.putExtra("position", position)
        notificationIntent.putExtra("title", title)
        notificationIntent.putExtra("body", Message)
        notificationIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                1,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                context, 1, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        mBuilder.setContentIntent(pendingIntent)
        val notification = mBuilder.build()
        notificationManager.notify(notificationId, notification)
    }

}