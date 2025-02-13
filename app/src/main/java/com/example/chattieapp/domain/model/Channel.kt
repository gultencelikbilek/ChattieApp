package com.example.chattieapp.domain.model

data class Channel(
    val id : Int,
    val name : String,
    val creadetAt : Long = System.currentTimeMillis()
)
