package com.seenu.dev.android.vibeplayer.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.seenu.dev.android.vibeplayer.domain.model.ScanConfig
import com.seenu.dev.android.vibeplayer.domain.repository.AppPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class AppPreferenceRepositoryImpl constructor(
    private val context: Context
) : AppPreferenceRepository {

    companion object {
        private const val PREFS_NAME = "app_preferences"
        private const val KEY_USER_DENIED_STORAGE_PERMISSION = "user_denied_storage_permission"
        private const val KEY_AUDIO_FILES_SCANNED_AND_INDEXED = "audio_files_scanned_and_indexed"
        private const val KEY_SCAN_CONFIG_MIN_SIZE = "scan_config_min_size"
        private const val KEY_SCAN_CONFIG_MIN_DURATION = "scan_config_min_duration"
        private const val IS_FIRST_LAUNCH = "is_first_launch"
    }

    private val userDeniedStoragePermissionKey =
        booleanPreferencesKey(KEY_USER_DENIED_STORAGE_PERMISSION)
    private val audioFilesScannedAndIndexedKey =
        booleanPreferencesKey(KEY_AUDIO_FILES_SCANNED_AND_INDEXED)
    private val scanConfigMinSizeKey = stringPreferencesKey(KEY_SCAN_CONFIG_MIN_SIZE)
    private val scanConfigMinDurationKey = stringPreferencesKey(KEY_SCAN_CONFIG_MIN_DURATION)

    private val Context.datastore by preferencesDataStore(PREFS_NAME)

    override fun isUserDeniedStoragePermission(): Flow<Boolean> {
        return with(context) {
            datastore.data.map { pref ->
                pref[userDeniedStoragePermissionKey] ?: false
            }
        }
    }

    override suspend fun setUserDeniedStoragePermission(denied: Boolean) {
        with(context) {
            datastore.edit { pref ->
                pref[userDeniedStoragePermissionKey] = denied
            }
        }
    }

    override fun isAudioFilesScannedAndIndexed(): Flow<Boolean> {
        return with(context) {
            datastore.data.map { pref ->
                pref[audioFilesScannedAndIndexedKey] ?: false
            }
        }
    }

    override suspend fun onAudioFilesScannedAndIndexed(scanned: Boolean) {
        with(context) {
            datastore.edit { pref ->
                pref[audioFilesScannedAndIndexedKey] = scanned
            }
        }
    }

    override fun getScanConfig(): Flow<ScanConfig> {
        return with(context) {
            datastore.data.map { pref ->
                ScanConfig(
                    minSize = pref[scanConfigMinSizeKey]?.let {
                        ScanConfig.MinSize.valueOf(it)
                    } ?: ScanConfig.MinSize.KB_100,
                    minDuration = pref[scanConfigMinDurationKey]?.let {
                        ScanConfig.MinDuration.valueOf(it)
                    } ?: ScanConfig.MinDuration.SEC_30,
                    isInitialScanDone = pref[audioFilesScannedAndIndexedKey] ?: false
                )
            }
        }
    }

    override suspend fun setScanConfig(scanConfig: ScanConfig) {
        with(context) {
            datastore.edit { pref ->
                pref[scanConfigMinSizeKey] = scanConfig.minSize.name
                pref[scanConfigMinDurationKey] = scanConfig.minDuration.name
            }
        }
    }

}