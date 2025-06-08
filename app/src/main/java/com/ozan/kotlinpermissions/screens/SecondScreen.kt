package com.ozan.kotlinpermissions.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ozan.kotlinpermissions.util.PermissionManager

@Composable
fun SecondScreen(onBack: () -> Unit) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri = uri }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) Toast.makeText(context, "Fotoğraf çekildi!", Toast.LENGTH_SHORT).show()
    }


    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val perm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

        if (isGranted) galleryLauncher.launch("image/*")
        else PermissionManager.handlePermissionResult(context, perm, false)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) cameraLauncher.launch(null)
        else PermissionManager.handlePermissionResult(context, Manifest.permission.CAMERA, false)
    }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                context.startActivity(Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION))
            } catch (e: Exception) {
                Toast.makeText(context, "Ses kayıt uygulaması bulunamadı.", Toast.LENGTH_SHORT).show()
            }
        } else PermissionManager.handlePermissionResult(context, Manifest.permission.RECORD_AUDIO, false)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        } else PermissionManager.handlePermissionResult(context, Manifest.permission.ACCESS_FINE_LOCATION, false)
    }

    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val pickIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            context.startActivity(pickIntent)
        } else PermissionManager.handlePermissionResult(context, Manifest.permission.READ_CONTACTS, false)
    }

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val smsIntent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("sms:") }
            context.startActivity(smsIntent)
        } else PermissionManager.handlePermissionResult(context, Manifest.permission.SEND_SMS, false)
    }

    val callPhonePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) context.startActivity(Intent(Intent.ACTION_DIAL))
        else PermissionManager.handlePermissionResult(context, Manifest.permission.CALL_PHONE, false)
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionManager.handlePermissionResult(context, Manifest.permission.POST_NOTIFICATIONS, false)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
                .border(2.dp, Color.Cyan)
                .clickable {
                    val perm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

                    PermissionManager.checkAndRequestPermission(
                        context, perm, storagePermissionLauncher
                    ) { galleryLauncher.launch("image/*") }
                }
        ) {
            val painter = if (selectedImageUri != null)
                rememberAsyncImagePainter(selectedImageUri)
            else rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data("https://via.placeholder.com/300.png")
                    .crossfade(true)
                    .build()
            )

            Image(painter, contentDescription = null, modifier = Modifier.fillMaxSize())

            Text(
                text = "Galeri",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .background(Color(0x80000000))
                    .padding(8.dp)
            )
        }


        val permissionList = listOf(
            "Ses Kaydı"      to Manifest.permission.RECORD_AUDIO,
            "Depolama"       to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else Manifest.permission.READ_EXTERNAL_STORAGE,
            "Konum"          to Manifest.permission.ACCESS_FINE_LOCATION,
            "Kamera"         to Manifest.permission.CAMERA,
            "Bildirim"       to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.POST_NOTIFICATIONS else "",
            "SMS"            to Manifest.permission.SEND_SMS,
            "Kişiler"        to Manifest.permission.READ_CONTACTS,
            "Telefon Arama"  to Manifest.permission.CALL_PHONE,
        )

        permissionList.forEach { (name, permission) ->
            if (permission.isNotEmpty()) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        when (name) {
                            "Kamera" -> PermissionManager.checkAndRequestPermission(
                                context, permission, cameraPermissionLauncher
                            ) { cameraLauncher.launch(null) }

                            "Depolama" -> PermissionManager.checkAndRequestPermission(
                                context, permission, storagePermissionLauncher
                            ) { galleryLauncher.launch("*/*") }

                            "Ses Kaydı" -> PermissionManager.checkAndRequestPermission(
                                context, permission, recordAudioPermissionLauncher
                            ) {
                                context.startActivity(Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION))
                            }

                            "Konum" -> PermissionManager.checkAndRequestPermission(
                                context, permission, locationPermissionLauncher
                            ) {
                                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }

                            "Kişiler" -> PermissionManager.checkAndRequestPermission(
                                context, permission, contactsPermissionLauncher
                            ) {
                                val pick = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                                context.startActivity(pick)
                            }

                            "SMS" -> PermissionManager.checkAndRequestPermission(
                                context, permission, smsPermissionLauncher
                            ) {
                                val sms = Intent(Intent.ACTION_VIEW, Uri.parse("sms:"))
                                context.startActivity(sms)
                            }

                            "Telefon Arama" -> PermissionManager.checkAndRequestPermission(
                                context, permission, callPhonePermissionLauncher
                            ) {
                                context.startActivity(Intent(Intent.ACTION_DIAL))
                            }

                            "Bildirim" -> PermissionManager.checkAndRequestPermission(
                                context, permission, notificationPermissionLauncher
                            )

                            else -> Unit
                        }
                    }
                ) { Text(name) }
            }
        }
    }
}
