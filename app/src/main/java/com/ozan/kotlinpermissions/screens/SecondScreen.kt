package com.ozan.kotlinpermissions.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

data class PermissionItem(
    val name: String,
    val manifestPermission: String
)

val permissionList = listOf(
    PermissionItem("Gallery", if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE),

    PermissionItem("Audio", Manifest.permission.RECORD_AUDIO),
    PermissionItem("Storage", if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE),

    PermissionItem("Location", Manifest.permission.ACCESS_FINE_LOCATION),
    PermissionItem("Camera", Manifest.permission.CAMERA),
    PermissionItem("Notification", if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.POST_NOTIFICATIONS else ""),

    PermissionItem("SMS", Manifest.permission.SEND_SMS),
    PermissionItem("Contacts", Manifest.permission.READ_CONTACTS),
    PermissionItem("Phone Call", Manifest.permission.CALL_PHONE),
)


@Composable
fun SecondScreen(onBack: () -> Unit) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity, Manifest.permission.CAMERA
            )
            if (!showRationale) {
                Toast.makeText(context, "Kalıcı olarak reddedildi. Ayarlardan açın.", Toast.LENGTH_LONG).show()

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun requestPermissionWithCheck(permission: String) {
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission) -> {
                permissionLauncher.launch(permission)
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }

    val dummyImageUri = "https://via.placeholder.com/300.png"

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
                .border(2.dp, Color.Cyan, RoundedCornerShape(8.dp))
                .clickable {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE

                    requestPermissionWithCheck(permission)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(dummyImageUri)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Photo Area",
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Gallery",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .background(Color(0x80000000))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        permissionList.drop(1).forEach { permissionItem ->
            if (permissionItem.manifestPermission.isNotEmpty()) {
                Button(
                    onClick = {
                        requestPermissionWithCheck(permissionItem.manifestPermission)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(permissionItem.name)
                }
            }
        }
    }
}





