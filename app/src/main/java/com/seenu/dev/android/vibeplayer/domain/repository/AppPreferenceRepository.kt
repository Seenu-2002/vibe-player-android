package com.seenu.dev.android.vibeplayer.domain.repository

import com.seenu.dev.android.vibeplayer.domain.model.ScanConfig
import kotlinx.coroutines.flow.Flow

interface AppPreferenceRepository {

    fun isUserDeniedStoragePermission(): Flow<Boolean>

    suspend fun setUserDeniedStoragePermission(denied: Boolean)

    fun isAudioFilesScannedAndIndexed(): Flow<Boolean>

    suspend fun onAudioFilesScannedAndIndexed(scanned: Boolean)

    fun getScanConfig(): Flow<ScanConfig>

    suspend fun setScanConfig(scanConfig: ScanConfig)

}