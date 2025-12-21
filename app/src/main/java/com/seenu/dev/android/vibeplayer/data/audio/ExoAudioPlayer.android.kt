package com.seenu.dev.android.vibeplayer.data.audio

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaSession
import com.seenu.dev.android.vibeplayer.domain.audio.AudioPlayer
import com.seenu.dev.android.vibeplayer.domain.audio.PlaybackState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single
import timber.log.Timber
import java.io.File

@Single(binds = [AudioPlayer::class])
class ExoAudioPlayer constructor(
    private val context: Context,
) : AudioPlayer {
    private val player by lazy {
        ExoPlayer.Builder(context)
            .setName("Vibe Player ExoPlayer")
            .build()
            .apply {
                addAnalyticsListener(EventLogger())
                prepare()
                addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        Timber.d(
                            "Events -> $events + Current Position: ${player.currentPosition} " +
                                    "Duration: ${player.duration} " +
                                    "IsPlaying: ${player.isPlaying}"
                        )
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        val state = if (isPlaying) {
                            PlaybackState.Playing
                        } else {
                            PlaybackState.Paused
                        }
                        _playbackState.tryEmit(state)
                    }
                })
            }
    }

    private val _playbackState: MutableStateFlow<PlaybackState> = MutableStateFlow(PlaybackState.Paused)
    override val playbackState: Flow<PlaybackState> = _playbackState.asStateFlow()

    override val currentPositionMs: Long
        get() = player.currentPosition

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    override fun setSource(path: String) {
        val uri = Uri.fromFile(File(path))
        player.setMediaItem(MediaItem.fromUri(uri))
    }

    override fun release() {
        // TODO
        player.release()
    }
}