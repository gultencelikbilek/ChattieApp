package com.example.chattieapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

//FCM'den gelen mesajları almak için kullanılır.
class FirebaseMessageService : FirebaseMessagingService() {

    //onMessageReceived(message: RemoteMessage) Metodu:
    //FCM’den bir mesaj alındığında otomatik olarak çalışır.
    //Mesajın bir bildirimi olup olmadığını kontrol eder (message.notification).
    //Eğer bildirim varsa, showNotification fonksiyonunu çağırarak bildirimi gösterir.
    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    fun showNotification(title: String?, message: String?) {
        //Firebase.auth.currentUser → Firebase Authentication kullanılarak oturum açmış olan mevcut kullanıcıyı alır.
        Firebase.auth.currentUser?.let {
            //Eğer title değişkeni kullanıcının adını içeriyorsa true döner.
            // Eğer message değişkeni kullanıcının adını içeriyorsa true döner.
            if (title?.contains(it.displayName.toString()) == true || message?.contains(it.displayName.toString()) == true) return
        }

        //NotificationManager kullanarak bir bildirim oluşturur ve gösterir.
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("burda","ife girmedi daha ")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("messages", "Messages", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            Log.d("burda","ife:düştü")

        } else {
            Log.d("burda","else:düştü")
        }

        //Random().nextInt(1000) ile her bildirim için rastgele bir notificationId belirler (aynı ID kullanılmadığından çakışma olmaz).
        val notificationId = Random().nextInt(1000)

        val notification = NotificationCompat.Builder(this, "messages")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.chat_logo)
            .build()
        notificationManager.notify(notificationId, notification)
    }
}