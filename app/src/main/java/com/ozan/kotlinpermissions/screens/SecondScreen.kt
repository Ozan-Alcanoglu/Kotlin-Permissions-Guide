// com/ozan/kotlinpermissions/screens/SecondScreen.kt
package com.ozan.kotlinpermissions.screens

import android.Manifest
import android.net.Uri
import android.os.Build
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ozan.kotlinpermissions.util.PermissionManager

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
                text = "Gallery",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .background(Color(0x80000000))
                    .padding(8.dp)
            )
        }

        permissionList.drop(1).forEach { permissionItem ->
            if (permissionItem.manifestPermission.isNotEmpty()) {
                Button(
                    onClick = {
                        PermissionManager.checkAndRequestPermission(
                            context = context,
                            permission = permissionItem.manifestPermission,
                            launcher = permissionLauncher
                        )
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
