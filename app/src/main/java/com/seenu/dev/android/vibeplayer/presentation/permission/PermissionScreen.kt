package com.seenu.dev.android.vibeplayer.presentation.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.seenu.dev.android.vibeplayer.presentation.design_system.PermissionRequiredCard
import com.seenu.dev.android.vibeplayer.presentation.utils.openAppSettings
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PermissionScreen(onAllowed: () -> Unit) {

    val viewModel: PermissionViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.hasAccessToFiles.collectLatest {
            if (it) {
                onAllowed()
            }
        }
    }

    val apiVersion = Build.VERSION.SDK_INT
    val permission = if (apiVersion >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_AUDIO
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    viewModel.onIntent(PermissionIntent.OnMusicReadPermissionGranted)
                } else {
                    viewModel.onIntent(PermissionIntent.OnMusicReadPermissionDenied)
                }
            })

    val activity = LocalActivity.current
    val hasPermission =
        activity?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    if (hasPermission) {
        viewModel.onIntent(PermissionIntent.OnMusicReadPermissionGranted)
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            PermissionRequiredCard(
                onAllowAccess = {
                    activity ?: return@PermissionRequiredCard
                    val shouldShowRationale =
                        activity.shouldShowRequestPermissionRationale(permission)

                    if (shouldShowRationale) {
                        permissionResultLauncher.launch(permission)
                    } else {
                        if (!viewModel.isUserDeclinedPermission()) {
                            permissionResultLauncher.launch(permission)
                        } else {
                            activity.openAppSettings()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}