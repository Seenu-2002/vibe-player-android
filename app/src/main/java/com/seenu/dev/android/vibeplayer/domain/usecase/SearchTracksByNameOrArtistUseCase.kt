package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import org.koin.core.annotation.Single

@Single
class SearchTracksByNameOrArtistUseCase constructor(
    private val repository: MusicRepository
) {

    suspend operator fun invoke(query: String) =
        repository.searchTracksByNameOrArtist(query)

}