package com.example.chattieapp.presentation.chat_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chattieapp.R
import com.example.chattieapp.domain.model.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ChatScreen(
    navController: NavController,
    channelId: String?,
    chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
) {
    val messageState = chatScreenViewModel.messageState.collectAsState()

    LaunchedEffect(key1 = true) {
        if (channelId != null) {
            Log.d("chat/channeelId", "$channelId")
            chatScreenViewModel.listenerForMessages(channelId)
        }
    }
    Scaffold(
        containerColor = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ChatMessages(messages = messageState.value, onSendMessage = {
                    if (channelId != null) {
                        chatScreenViewModel.sendMessage(channelId, it)
                    }
                })
            }
        }
    }
}

@Composable
fun ChatMessages(
    messages: List<Message>,
    onSendMessage: (String) -> Unit
) {
    var msg by remember {
        mutableStateOf("")
    }
    val hideKeyBoardController = LocalSoftwareKeyboardController.current
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp)
                .background(Color.DarkGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                msg = ""
            }) {
                Log.d("msg", "$msg")
                Icon(
                    painter = painterResource(id = R.drawable.attach), contentDescription = null,
                    tint = colorResource(
                        id = R.color.purple
                    )
                )
            }

            TextField(value = msg,
                onValueChange = {
                    msg = it
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = stringResource(id = R.string.type_a_message)) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyBoardController?.hide()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray,
                    unfocusedContainerColor = Color.DarkGray,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White
                )
            )
            IconButton(onClick = {
                onSendMessage(msg)
                msg = ""
            }) {
                Log.d("msg", "$msg")
                Icon(
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = null,
                    tint = colorResource(
                        id = R.color.purple
                    )
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        colorResource(id = R.color.purple)
    } else {
        Color.DarkGray
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
        Row(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isCurrentUser) {
                Image(
                    painter = painterResource(id = R.drawable.woman),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = message.message,
                color = Color.White,
                modifier = Modifier
                    .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
            Log.d("ChatBubble", "Mesaj: ${message.message}")
        }
    }
}

