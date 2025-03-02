package com.example.chattieapp.presentation.chat_screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chattieapp.domain.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.chattieapp.R
import com.google.auth.oauth2.GoogleCredentials
import dagger.hilt.android.qualifiers.ApplicationContext


@HiltViewModel
class ChatScreenViewModel @Inject constructor(@ApplicationContext val context: Context) :
    ViewModel() {

    private val _messageState = MutableStateFlow<List<Message>>(emptyList())
    val messageState = _messageState.asStateFlow()
    val db = Firebase.database

    fun sendMessage(channelId: String, messageText: String, image: String? = null) {
        val messageKey = db.reference.push().key

        if (messageKey == null) {
            Log.e("Firebase", "Push Key is null, message cannot be sent!")
            return
        }

        val message = Message(
            messageKey,
            Firebase.auth.currentUser?.uid ?: "",
            messageText,
            System.currentTimeMillis(),
            Firebase.auth.currentUser?.displayName ?: "",
            null,
            image
        )

        db.getReference("messages")
            .child(channelId)
            .child(message.id)
            .setValue(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase", "Message successfully sent!")
                } else {
                    Log.e("Firebase", "Message sending failed: ${task.exception?.message}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Message sending error: ${exception.message}")
            }
            .addOnCompleteListener {
                Log.d("postnotificaiton","${message.senderName } $messageText")
                postNotificationUsers(channelId, message.senderName, messageText ?: "")
            }
    }

    fun sendImageMessage(uri: Uri, channelId: String? = null) {
        val imageRef = Firebase.storage.reference.child("images/${UUID.randomUUID()}")
        imageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = Firebase.auth.currentUser
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        if (channelId != null) {
                            sendMessage(channelId, null.toString(), downloadUri.toString())
                        }
                    }
                }
            }
    }


    fun listenerForMessages(channelId: String) {
        db.getReference("messages").child(channelId).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach { data ->
                        val message = data.getValue(Message::class.java)
                        message?.let {
                            list.add(message)
                            Log.d("list:add", "$it")
                        }
                    }
                    _messageState.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    //handle error
                }
            })
        subscribeForNotification(channelId)
    }

    // subscribeForNotification(channelId: String?) →
    //channelId parametresi ile belirlenen bir gruba bildirim almak için abone olunmasını sağlayan bir fonksiyondur.
    private fun subscribeForNotification(channelId: String?) {
        // Firebase Cloud Messaging (FCM) örneğini alıp, belirli bir konuya abone olma işlemi başlatılıyor
        FirebaseMessaging.getInstance().subscribeToTopic("group_$channelId")
            .addOnCompleteListener {
                // Eğer abone olma işlemi başarılı olursa
                if (it.isSuccessful) {
                    Log.d("chatScreenViewmodel:", "Subscribe to topic : group_$channelId")
                } else {
                    // Eğer işlem başarısız olursa
                    Log.d("chatScreenViewmodel:", "Failed to topic : group_$channelId")
                }
            }
    }

    fun postNotificationUsers(channelID: String, senderName: String, messageContent: String) {
        val fcmUrl = "https://fcm.googleapis.com/v1/projects/chattierapp/messages:send"

        // JSON nesnesi oluşturuluyor, bu nesne HTTP isteği içinde gönderilecek.
        val jsonBody = JSONObject().apply {
            put("message", JSONObject().apply {
                put("topic", "group_$channelID")

                // Bildirim içeriği oluşturuluyor.
                put("notification", JSONObject().apply {
                    put("title", "New message in $channelID")
                    put("body", "$senderName $messageContent")
                })
            })
        }
        val requestBody = jsonBody.toString()

        // Volley kullanarak bir HTTP POST isteği oluşturuluyor.
        val request = object : StringRequest(
            Method.POST, fcmUrl,  // HTTP POST isteği, belirtilen URL'ye gönderilecek.
            { response ->
                Log.d("VolleyResponse", "Response: $response") // Başarılı yanıt
            },
            { error ->
                Log.e("VolleyError", "Error: ${error.message}") // Hata durumu
            }) {

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${getAccesToken()}"
                headers["Content_type"] = "applicaiton/json"
                return headers
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private fun getAccesToken(): String {
        val inputStream = context.resources.openRawResource(R.raw.chattierapp)
        val googleCreds = GoogleCredentials.fromStream(inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        return googleCreds.refreshAccessToken().tokenValue
    }
}