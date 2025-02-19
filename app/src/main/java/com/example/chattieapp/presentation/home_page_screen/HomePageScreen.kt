package com.example.chattieapp.presentation.home_page_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chattieapp.R
import com.example.chattieapp.presentation.navigation.Screen

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
                }) {
                Text(text = "Add Channel", modifier = Modifier.padding(16.dp), color = White)
            }
        },
        containerColor = Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .clip(RoundedCornerShape(25.dp))
                .fillMaxSize()
        ) {
            LazyColumn {
                item {
                    Text(
                        text = stringResource(id = R.string.messages),
                        color = Gray,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                item {
                    TextField(
                        value = "", onValueChange = {},
                        placeholder = {
                            Text(text = stringResource(id = R.string.search))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 5.dp)
                            .clip(
                                RoundedCornerShape(40.dp)
                            ),
                        textStyle = TextStyle(
                            color = LightGray
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = DarkGray,
                            unfocusedContainerColor = DarkGray,
                            focusedTextColor = Gray,
                            unfocusedTextColor = Gray,
                            focusedPlaceholderColor = Gray,
                            unfocusedPlaceholderColor = Gray,
                            focusedIndicatorColor = Gray
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                    )
                }

                items(channelState.value) { channel ->
                    Column {
                        ChannelItem(channelName = channel.name) {
                            Log.d("chat/channeelId", "${channel.id}")
                            navController.navigate(Screen.ChatScreen.route + "chat/${channel.id}")
                        }
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
fun ChannelItem(channelName: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkGray)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.Yellow.copy(alpha = 0.3f))

        ) {
            Text(
                text = channelName[0].uppercase(),
                color = White,
                style = TextStyle(fontSize = 35.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Text(text = channelName, modifier = Modifier.padding(8.dp), color = White)
    }

}

@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    var channelName by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.padding(8.dp),
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