package com.example.chattieapp

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
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
            showNotification(it.title,it.body)
        }
    }

    fun showNotification(title : String?, message: String?){
        //NotificationManager kullanarak bir bildirim oluşturur ve gösterir.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Random().nextInt(1000) ile her bildirim için rastgele bir notificationId belirler (aynı ID kullanılmadığından çakışma olmaz).
        val notificationId = Random().nextInt(1000)

        val notification = NotificationCompat.Builder(this,"messages")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.chat_logo)
            .build()
        notificationManager.notify(notificationId,notification)
    }
}