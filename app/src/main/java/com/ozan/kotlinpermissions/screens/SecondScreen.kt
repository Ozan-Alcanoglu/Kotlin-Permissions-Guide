package com.ozan.kotlinpermissions.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
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
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri = uri }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            PermissionManager.handlePermissionResult(context, permission, isGranted)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            Toast.makeText(context, "Fotoğraf çekildi!", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            PermissionManager.handlePermissionResult(context, Manifest.permission.CAMERA, isGranted)
        }
    }

    fun launchIntent(action: String, type: String? = null) {
        val intent = Intent(action)
        if (type != null) {
            intent.type = type
        }
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Uygulama bulunamadı.", Toast.LENGTH_SHORT).show()
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
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE

                    PermissionManager.checkAndRequestPermission(
                        context = context,
                        permission = permission,
                        launcher = permissionLauncher,
                        onGranted = {
                            galleryLauncher.launch("image/*")
                        }
                    )
                }
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data("https://via.placeholder.com/300.png")
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Placeholder",
                    modifier = Modifier.fillMaxSize()
                )
            }

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
            "Ses Kaydı" to Manifest.permission.RECORD_AUDIO,
            "Depolama" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE,
            "Konum" to Manifest.permission.ACCESS_FINE_LOCATION,
            "Kamera" to Manifest.permission.CAMERA,
            "Bildirim" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.POST_NOTIFICATIONS else "",
            "SMS" to Manifest.permission.SEND_SMS,
            "Kişiler" to Manifest.permission.READ_CONTACTS,
            "Telefon Arama" to Manifest.permission.CALL_PHONE,
        )

        permissionList.forEach { (name, permission) ->
            if (permission.isNotEmpty()) {
                Button(
                    onClick = {
                        when (name) {
                            "Kamera" -> {
                                PermissionManager.checkAndRequestPermission(
                                    context, permission, cameraPermissionLauncher,
                                    onGranted = { cameraLauncher.launch(null) }
                                )
                            }
                            "Ses Kaydı" -> {
                                PermissionManager.checkAndRequestPermission(
                                    context, permission, permissionLauncher,
                                    onGranted = {
                                        launchIntent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
                                    }
                                )
                            }
                            "Depolama" -> {
                                PermissionManager.checkAndRequestPermission(
                                    context, permission, permissionLauncher,
                                    onGranted = {
                                        galleryLauncher.launch("*/*") // tüm dosyaları aç
                                    }
                                )
                            }
                            "SMS" -> {
                                PermissionManager.checkAndRequestPermission(
                                    context, permission, permissionLauncher,
                                    onGranted = {
                                        val smsIntent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse("sms:")
                                        }
                                        context.startActivity(smsIntent)
                                    }
                                )
                            }
                            "Telefon Arama" -> {
                                PermissionManager.checkAndRequestPermission(
                                    context, permission, permissionLauncher,
                                    onGranted = {
                                        val callIntent = Intent(Intent.ACTION_DIAL)
                                        context.startActivity(callIntent)
                                    }
                                )
                            }
                            else -> {
                                PermissionManager.checkAndRequestPermission(
                                    context, permission, permissionLauncher
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(name)
                }
            }
        }
    }
}
