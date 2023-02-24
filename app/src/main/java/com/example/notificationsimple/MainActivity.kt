package com.example.notificationsimple

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn: Button = findViewById(R.id.btn)

        // Создание и регистрация канала уведомлений api 26+
        val channelId = "My_Channel_ID"
        createNotificationChannel(channelId)


        // Создайте явное намерение
        val intent = Intent(this,MainActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)

        btn.setOnClickListener{
            val notificationBuilder = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Title: API LEVEL " + Build.VERSION.SDK_INT)
                .setContentText("UUID: " + UUID.randomUUID())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(LongArray(0))


            with(NotificationManagerCompat.from(this)){
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@setOnClickListener
                }
                notify(1, notificationBuilder.build())
            }
        }
    }


    private fun createNotificationChannel(channelId:String) {
        // Создаем NotificationChannel, но только в API 26+ (Android 8.0),
        // потому что класс NotificationChannel является новым и отсутствует в библиотеке поддержки.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "My Channel"
            val channelDescription = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId,name,importance)
            channel.apply {
                description = channelDescription
            }

            // Наконец регистрируем канал в системе
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}