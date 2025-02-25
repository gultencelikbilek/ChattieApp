package com.example.chattieapp.presentation.chat_screen

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chattieapp.R
import com.example.chattieapp.domain.model.Message
import com.example.chattieapp.presentation.home_page_screen.ChannelItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    navController: NavController,
    channelId: String?,
    channelName : String?,
    chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
) {
    val messageState = chatScreenViewModel.messageState.collectAsState()

    val chooserDialog = remember {
        mutableStateOf(false)
    }
    val cameraImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val cameraImageLauncher =
    //rememberLauncherForActivityResult ile İzin Kontrolü
        //Bu yapı, kamera izni (CAMERA) istemek için kullanılıyor.
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { succes ->
            if (succes) {
                cameraImageUri.value?.let {
                    // Resim başarıyla çekildi, işlem yapabilirsin
                    chatScreenViewModel.sendImageMessage(it, channelId)
                }
            }
        }
    val imageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                chatScreenViewModel.sendImageMessage(it, channelId)
            }
        }

    fun createImageUri(): Uri {
        //timeStamp ekleyerek dosya adını benzersiz hale getiriyor.
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        //Resimlerin kaydedileceği klasörü belirliyor (storageDir)
        val storageDir = ContextCompat.getExternalFilesDirs(
            navController.context,
            Environment.DIRECTORY_PICTURES
        ).first()
        //Bu dosyanın URI'sini oluşturuyor (FileProvider.getUriForFile())
        //URI güvenli bir şekilde oluşturuluyor ve diğer uygulamalarla paylaşılabilir hale geliyor.
        return FileProvider.getUriForFile(navController.context,
            "${navController.context.packageName}.provider",

            //Geçici bir resim dosyası oluşturuyor (File.createTempFile())
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {

                //Dosyanın kendi URI'sini cameraImageUri değişkenine atıyor.
                //Böylece URI'yi sonradan kullanabiliriz (örn. görüntülemek için).
                cameraImageUri.value = Uri.fromFile(this)
            }
        )
    }

    val permisionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

                //Eğer izin verilirse (if (isGranted))
                //
                //Kamera açılıyor ve çekilecek fotoğraf için URI belirleniyor.
                //cameraImageLauncher.launch(createImageUri()) →
                // Kamera başlatılıyor ve çekilen fotoğraf createImageUri() fonksiyonunda belirlenen yere kaydediliyor.
                cameraImageLauncher.launch(createImageUri())
            }
        }

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
            ChatMessages(
                messages = messageState.value,
                channelName,
                onSendMessage = {
                    if (channelId != null) {
                        chatScreenViewModel.sendMessage(channelId, it)
                    }
                },
                onImageClicked = {
                    chooserDialog.value = true
                }
            )
        }

        if (chooserDialog.value) {
            ContentSelectionDialog(
                onCameraSelected = {
                    chooserDialog.value = false
                    if (navController.context.checkSelfPermission(Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        cameraImageLauncher.launch(createImageUri())
                    } else {
                        permisionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onGallerySelected = {
                    chooserDialog.value = false
                    imageLauncher.launch("image/*")
                }
            )
        }
    }
}

@Composable
fun ChatMessages(
    messages: List<Message>,
    channelName: String?,
    onSendMessage: (String) -> Unit,
    onImageClicked: () -> Unit
) {
    var msg by remember {
        mutableStateOf("")
    }
    val hideKeyBoardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                if (channelName != null) {
                    ChannelItem(channelName = channelName){}
                }
            }
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.DarkGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                msg = ""
                onImageClicked()
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
fun ContentSelectionDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit) {

    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = onCameraSelected) {
                Text(text = stringResource(id = R.string.camera))
            }
        },
        dismissButton = {
            TextButton(onClick = onGallerySelected) {
                Text(text = stringResource(id = R.string.gallery))
            }
        },
        title = { Text(text = stringResource(id = R.string.select_your_source)) },
        text = { Text(text = stringResource(id = R.string.gallery_or_camera)) })

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

            Box(
                modifier = Modifier
                    .background(
                        color = bubbleColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {


                if (message.imageUrl != null) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    message.message?.let {
                        Text(
                            text = it.trim(),
                            color = Color.White
                        )
                    }
                }
            }
            Log.d("ChatBubble", "Mesaj: ${message.message}")
        }
    }
}

