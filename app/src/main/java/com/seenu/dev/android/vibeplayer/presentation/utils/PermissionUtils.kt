package com.seenu.dev.android.vibeplayer.presentation.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.content.PermissionChecker

class PermissionUtils {

    fun hasRequiredPermissions(context: Context): Boolean {
        return getRequiredPermission().all { permission ->
            PermissionChecker.checkSelfPermission(
                context,
                permission
            ) == PermissionChecker.PERMISSION_GRANTED
        }
    }

    private fun getRequiredPermission(): List<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

}