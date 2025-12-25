package com.seenu.dev.android.vibeplayer.presentation.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.vibeplayer.domain.repository.AppPreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PermissionViewModel constructor(
    private val appPreferenceRepository: AppPreferenceRepository
) : ViewModel() {

    val isUserDeniedStoragePermission = appPreferenceRepository.isUserDeniedStoragePermission()

    private val _hasAccessToFiles: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val hasAccessToFiles: StateFlow<Boolean> = _hasAccessToFiles.asStateFlow()

    fun onIntent(intent: PermissionIntent) {
        viewModelScope.launch {
            when (intent) {
                PermissionIntent.OnMusicReadPermissionGranted -> {
                    _hasAccessToFiles.value = true
                    appPreferenceRepository.setUserDeniedStoragePermission(false)
                }

                PermissionIntent.OnMusicReadPermissionDenied -> {
                    _hasAccessToFiles.value = false
                    appPreferenceRepository.setUserDeniedStoragePermission(true)
                }
            }
        }
    }

}

sealed interface PermissionIntent {
    data object OnMusicReadPermissionGranted : PermissionIntent
    data object OnMusicReadPermissionDenied : PermissionIntent
}