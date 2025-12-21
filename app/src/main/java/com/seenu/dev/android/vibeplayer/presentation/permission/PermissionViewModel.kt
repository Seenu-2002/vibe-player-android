package com.seenu.dev.android.vibeplayer.presentation.permission

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PermissionViewModel constructor(
    private val permissionPref: PermissionPreference
) : ViewModel() {

    private val _hasAccessToFiles: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasAccessToFiles: StateFlow<Boolean> = _hasAccessToFiles.asStateFlow()

    fun onIntent(intent: PermissionIntent) {
        when (intent) {
            PermissionIntent.OnMusicReadPermissionGranted -> {
                _hasAccessToFiles.value = true
            }

            PermissionIntent.OnMusicReadPermissionDenied -> {
                _hasAccessToFiles.value = false
                permissionPref.onUserDeclinedPermission(true)
            }
        }
    }

    fun isUserDeclinedPermission(): Boolean {
        return permissionPref.userDeclinedPermission()
    }

}

sealed interface PermissionIntent {
    data object OnMusicReadPermissionGranted : PermissionIntent
    data object OnMusicReadPermissionDenied : PermissionIntent
}