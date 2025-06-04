package com.ozan.kotlinpermissions.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(){

    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.DarkGray)
            .verticalScroll(scrollState)
    ) {

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(60.dp)
            ) {
            Text("picture")
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(60.dp)
        ) {
            Text("voice")
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(60.dp)
        ) {
            Text("depolama")
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(60.dp)
        ) {
            Text("fotoğraf")
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(60.dp)
        ) {
            Text("fotoğraf")
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(60.dp)
        ) {
            Text("fotoğraf")
        }

    }



}

@Preview(showBackground = true)
@Composable
fun Test(){
    MainScreen()
}
