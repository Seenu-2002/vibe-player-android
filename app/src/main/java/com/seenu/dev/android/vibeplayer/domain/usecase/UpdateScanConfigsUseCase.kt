package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.model.ScanConfig
import com.seenu.dev.android.vibeplayer.domain.repository.AppPreferenceRepository
import org.koin.core.annotation.Single

@Single
class UpdateScanConfigsUseCase constructor(
    private val repository: AppPreferenceRepository
) {

    suspend operator fun invoke(scanConfig: ScanConfig) {
        repository.setScanConfig(scanConfig)
    }

}