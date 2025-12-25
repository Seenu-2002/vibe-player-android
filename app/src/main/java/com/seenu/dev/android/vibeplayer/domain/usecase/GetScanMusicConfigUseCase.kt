package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.model.ScanConfig
import com.seenu.dev.android.vibeplayer.domain.repository.AppPreferenceRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetScanMusicConfigUseCase constructor(
    private val repository: AppPreferenceRepository
) {

    fun invoke(): Flow<ScanConfig> = repository.getScanConfig()

}