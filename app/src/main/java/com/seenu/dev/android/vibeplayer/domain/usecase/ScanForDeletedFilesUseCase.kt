package com.seenu.dev.android.vibeplayer.domain.usecase

import com.seenu.dev.android.vibeplayer.domain.repository.MusicRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import java.io.File

@Single
class ScanForDeletedFilesUseCase constructor(
    private val musicRepository: MusicRepository
) {

    operator fun invoke() {
        GlobalScope.launch {
            var offset = 0
            val count = 100
            var tracks = musicRepository.getScannedTracks(offset, count)
            while (tracks.isNotEmpty()) {
                val trackIdsToBeDeleted = mutableListOf<Long>()
                for (track in tracks) {
                    val file = File(track.filePath)
                    if (!file.exists()) {
                        trackIdsToBeDeleted.add(track.id)
                    }
                }
                if (trackIdsToBeDeleted.isNotEmpty()) {
                    musicRepository.deleteTrackByIds(*trackIdsToBeDeleted.toLongArray())
                }

                // If less than count tracks were returned, we've reached the end
                if (tracks.size != count) {
                    break
                }

                offset += count
                tracks = musicRepository.getScannedTracks(offset, count)
            }
        }
    }

}