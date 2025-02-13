package com.example.chattieapp.presentation.home_page_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomePageScreen(navController: NavController) {

    Scaffold(
        content = {paddingValues ->
            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()){
                LazyColumn {
                    item {
                        Column {

                        }
                    }
                }
            }
        }
    )
}