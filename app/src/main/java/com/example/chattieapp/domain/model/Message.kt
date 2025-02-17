package com.example.chattieapp.domain.model

data class Message(
    val id : String ="",
    val senderId : String = "",
    val message : String = "",
    val creadetAt : Long = System.currentTimeMillis(),
    val senderName : String = "",
    val senderImage : String? = null,
    val imageUrl : String? = null
)
