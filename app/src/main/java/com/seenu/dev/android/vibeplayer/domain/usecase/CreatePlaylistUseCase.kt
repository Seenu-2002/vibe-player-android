package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.exceptions.PlaylistAlreadyExistException
import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import org.koin.core.annotation.Single

@Single
class CreatePlaylistUseCase constructor(
    private val musicRepository: MusicRepository
) {

    suspend operator fun invoke(name: String): Long {
        if (name.isBlank()) {
            throw IllegalArgumentException("Playlist name cannot be blank")
        }

        if (musicRepository.isPlaylistExists(name)) {
            throw PlaylistAlreadyExistException(name)
        }

        return musicRepository.createPlaylist(name)
    }

}
