package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class ScanMusicInDiskUseCase constructor(
    private val repository: MusicRepository
) {

    suspend operator fun invoke(config: ScanMusicConfig): Int {
        val scanResults = repository.scan(
            minDurationSeconds = config.minDurationInSeconds,
            minFileSizeInKb = config.minSizeInKb
        )
        Timber.d("Found ${scanResults.size} tracks")
        repository.updateScannedMusicList(scanResults)
        return scanResults.size
    }

}

data class ScanMusicConfig constructor(
    val minSizeInKb: Long,
    val minDurationInSeconds: Long
)