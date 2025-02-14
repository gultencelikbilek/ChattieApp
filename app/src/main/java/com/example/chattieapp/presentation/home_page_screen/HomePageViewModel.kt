package com.example.chattieapp.presentation.home_page_screen

import androidx.lifecycle.ViewModel
import com.example.chattieapp.domain.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor() : ViewModel() {

    //Realtime database erişim sağlandı.
    val firebaseDatabase = Firebase.database
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channel = _channels.asStateFlow()

    init {
        getChannels()
    }
    private fun getChannels(){
        //channel database erişim sağlanıyor.
        // .get() çağrısı ile bu koleksiyondaki tüm veriler alınır.
        //addOnSuccessListener ile veriler başarıyla alındığında yapılacak işlemleri tanımlıyorsunuz.
        firebaseDatabase.getReference("channel").get().addOnSuccessListener {
            val list = mutableListOf<Channel>()
            it.children.forEach {data->
                //data burada, Firebase'den gelen veriyi temsil ediyor.
                //.children ile bu veri nesnesinin alt öğelerine erişiyorsunuz.
                // Bu genellikle veritabanındaki her bir çocuk öğesini (yani her bir kanal verisini) temsil eder.
                val channel = Channel(data.key!!, data.value!!.toString())
                list.add(channel)
            }
            _channels.value = list
        }
    }

    fun addChannel(name:String){
        val key = firebaseDatabase.getReference("channel").push().key
        firebaseDatabase.getReference("channel").child(key!!).setValue(name).addOnSuccessListener {
            getChannels()
        }
    }
}