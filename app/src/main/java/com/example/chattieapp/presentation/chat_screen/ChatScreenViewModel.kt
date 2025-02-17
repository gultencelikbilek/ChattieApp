package com.example.chattieapp.presentation.chat_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chattieapp.domain.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(): ViewModel() {

    private val _messageState = MutableStateFlow<List<Message>>(emptyList())
    val messageState = _messageState.asStateFlow()
    val db = Firebase.database

    fun sendMessage(channelId: String, messageText: String) {
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
            null
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
    }


    fun listenerForMessages(channelId : String){
        db.getReference("messages").child(channelId).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach{data ->
                        val message = data.getValue(Message::class.java)
                            message?.let {
                                list.add(message)
                                Log.d("list:add","$it")
                            }
                    }
                    _messageState.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    //handle error
                }
            })
    }

}