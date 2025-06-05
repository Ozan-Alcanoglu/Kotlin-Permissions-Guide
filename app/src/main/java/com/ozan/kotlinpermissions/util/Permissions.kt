package com.ozan.kotlinpermissions.util


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {

    fun checkAndRequestPermission(
        context: Context,
        permission: String,
        launcher: ActivityResultLauncher<String>,
        onGranted: (() -> Unit)? = null
    ) {
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show()
                onGranted?.invoke()

            }

            ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission) -> {
                launcher.launch(permission)
            }

            else -> {
                launcher.launch(permission)
            }
        }
    }

    fun handlePermissionResult(
        context: Context,
        permission: String,
        isGranted: Boolean
    ) {
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity, permission
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
}
