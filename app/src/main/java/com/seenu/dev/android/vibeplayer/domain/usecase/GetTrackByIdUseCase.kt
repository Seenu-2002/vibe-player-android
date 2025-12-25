package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.model.Track
import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import org.koin.core.annotation.Single

@Single
class GetTrackByIdUseCase constructor(
    private val repository: MusicRepository
) {

    suspend operator fun invoke(trackId: Long): Track? =
        repository.getTrackById(trackId)

}