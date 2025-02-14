package com.example.chattieapp.presentation.home_page_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(
    navController: NavController,
    homePageViewModel: HomePageViewModel = hiltViewModel()
) {

    val channelState = homePageViewModel.channel.collectAsState()
    var addChannel by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    Scaffold(
        floatingActionButton = {
            Box(modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Blue)
                .clickable {
                    addChannel = true
                }){
                Text(text = "Add Channel", modifier = Modifier.padding(16.dp), color = White)
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .clip(RoundedCornerShape(25.dp))
                .fillMaxSize()
        ) {
            LazyColumn {
                items(channelState.value) { channel ->
                    Column {
                        Text(
                            text = channel.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Red.copy(alpha = 0.3f))
                                .clickable {
                                    // navController.navigate("chat/${channel.id}")
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
        if (addChannel) {
            ModalBottomSheet(
                onDismissRequest = { addChannel = false },
                sheetState = sheetState
            ) {
                AddChannelDialog {
                    homePageViewModel.addChannel(it)
                    addChannel = false
                }

            }
        }
    }


}

@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    var channelName by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Add Channel")
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            value = channelName, onValueChange = {
                channelName = it
            },
            label = {
                Text(text = "Channel Name")
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = { onAddChannel(channelName) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Add")

        }
    }
}