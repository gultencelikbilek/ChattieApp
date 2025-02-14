package com.example.chattieapp.domain.model

data class Channel(
    val id : String = "",
    val name : String,
    val creadetAt : Long = System.currentTimeMillis()
)
