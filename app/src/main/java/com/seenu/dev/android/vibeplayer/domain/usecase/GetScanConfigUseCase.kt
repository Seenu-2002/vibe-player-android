package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.repository.AppPreferenceRepository
import org.koin.core.annotation.Single

@Single
class GetScanConfigUseCase constructor(
    private val repository: AppPreferenceRepository
) {

    operator fun invoke() = repository.getScanConfig()

}