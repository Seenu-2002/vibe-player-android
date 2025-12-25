package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.repository.AppPreferenceRepository
import org.koin.core.annotation.Single

@Single
class MarkInitialTrackScanCompletedUseCase constructor(
    private val repository: AppPreferenceRepository
) {

    suspend operator fun invoke() {
        repository.onAudioFilesScannedAndIndexed(true)
    }

}