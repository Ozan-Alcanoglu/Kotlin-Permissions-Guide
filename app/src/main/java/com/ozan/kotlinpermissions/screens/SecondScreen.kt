package com.ozan.kotlinpermissions.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun SecondScreen(onBack: ()-> Unit) {
    val scrollState = rememberScrollState()

    val dummyImageUri = "https://via.placeholder.com/300.png"
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
                .border(2.dp, Color.Cyan, RoundedCornerShape(8.dp))
                .clickable {  },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(dummyImageUri)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Fotoğraf Alanı",
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Add Photo",
                color = Color.White,
                fontSize = 24.sp,
                modifier = Modifier
                    .background(Color(0x80000000))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }


        listOf(
            "Gallery", "Audio", "Storage", "Location", "Camera", "Notification", "SMS", "Contacts", "Phone Call"
        ).forEach { label ->
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(label)
            }
        }
    }
}



