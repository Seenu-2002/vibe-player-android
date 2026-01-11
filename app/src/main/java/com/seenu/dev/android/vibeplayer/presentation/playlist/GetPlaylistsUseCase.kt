package com.seenu.dev.android.vibeplayer.presentation.playlist

import com.seenu.dev.android.vibeplayer.domain.model.PlaylistWithSongsCount
import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetPlaylistsUseCase constructor(
    private val repository: MusicRepository
) {

    operator fun invoke(): Flow<List<PlaylistWithSongsCount>> {
        return repository.getPlaylistsWithCountFlow()
    }

}