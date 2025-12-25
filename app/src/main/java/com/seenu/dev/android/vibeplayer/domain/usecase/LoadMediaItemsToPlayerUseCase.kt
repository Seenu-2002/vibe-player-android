package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.audio.AudioPlayer
import com.seenu.dev.android.vibeplayer.domain.model.Track
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class LoadMediaItemsToPlayerUseCase constructor(
    private val audioPlayer: AudioPlayer,
) {

    operator fun invoke(tracks: List<Track>) {
        Timber.d("Loading ${tracks.size} tracks into audio player")
        audioPlayer.loadTracks(
            tracks.map { it.filePath }
        )
    }

}