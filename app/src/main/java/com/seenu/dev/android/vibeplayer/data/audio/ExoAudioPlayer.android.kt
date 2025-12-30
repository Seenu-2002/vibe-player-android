package com.seenu.dev.android.vibeplayer.data.audio

import android.R.attr.mode
import android.content.Context
import android.net.Uri
import android.os.Looper
import android.provider.MediaStore
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import com.seenu.dev.android.vibeplayer.domain.audio.AudioPlayer
import com.seenu.dev.android.vibeplayer.domain.audio.PlaybackState
import com.seenu.dev.android.vibeplayer.domain.model.RepeatMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Single
import timber.log.Timber
import java.io.File

@Single(binds = [AudioPlayer::class])
class ExoAudioPlayer constructor(
    private val context: Context,
) : AudioPlayer {

    private val loadedTracks = mutableListOf<String>()
    private val player by lazy {
        ExoPlayer.Builder(context)
            .setName("Vibe Player ExoPlayer")
            .setLooper(Looper.getMainLooper())
            .build()
            .apply {
                addAnalyticsListener(EventLogger())
                addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                            _playbackState.trySend(PlaybackState.TrackChange)
                        }

                        if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)) {
                            _playbackState.trySend(PlaybackState.SeekbarJump)
                        }

                        if (events.contains(Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED)) {
                            _playbackState.trySend(PlaybackState.ShuffleModeChange)
                        }

                        if (events.contains(Player.EVENT_REPEAT_MODE_CHANGED)) {
                            _playbackState.trySend(PlaybackState.RepeatModeChange)
                        }
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        val state = if (isPlaying) {
                            PlaybackState.Playing
                        } else {
                            PlaybackState.Paused
                        }
                        _playbackState.trySend(state)
                    }
                })
            }
    }

    private val _playbackState: Channel<PlaybackState> = Channel(Channel.BUFFERED)
    override val playbackState: Flow<PlaybackState> = _playbackState.receiveAsFlow()

    override val currentPositionMs: Long
        get() = player.currentPosition

    override val isShuffleEnabled: Boolean
        get() = player.shuffleModeEnabled

    override val repeatMode: RepeatMode
        get() = when (player.repeatMode) {
            Player.REPEAT_MODE_ALL -> RepeatMode.ALL
            Player.REPEAT_MODE_ONE -> RepeatMode.ONE
            else -> RepeatMode.NONE
        }

    override fun play() {
        Timber
            .d("Playing track at index: ${player.currentMediaItemIndex}")
        player.play()
    }

    override fun playTrack(index: Int) {
        Timber
            .d("Playing track at index: $index -> total track count = ${player.mediaItemCount}")
        player.seekTo(
            index, C.TIME_UNSET
        )
        player.play()
    }

    override fun pause() {
        Timber.d("Pausing playback")
        player.pause()
    }

    override fun seekTo(positionMs: Long) {
        Timber.d("Seeking to position: $positionMs ms")
        player.seekTo(positionMs)
    }

    override fun setSource(path: String) {
        Timber.d("Setting source to path: $path")
        player.setMediaItem(MediaItem.fromUri(path))
    }

    override fun hasNext(): Boolean {
        Timber.d("Checking if there is a next track")
        return player.hasNextMediaItem()
    }

    override fun hasPrevious(): Boolean {
        Timber.d("Checking if there is a previous track")
        return player.hasPreviousMediaItem()
    }

    override fun playNext() {
        Timber.d("Playing next track")
        player.seekToNextMediaItem()
        player.play()
    }

    override fun playPrevious() {
        Timber.d("Playing previous track")
        player.seekToPreviousMediaItem()
        player.play()
    }

    override fun currentTrackIndex(): Int {
        Timber
            .d("Current track index: ${player.currentMediaItemIndex}")
        return player.currentMediaItemIndex
    }

    override fun clearTracks() {
        Timber.d("Clearing all loaded tracks")
        player.clearMediaItems()
        loadedTracks.clear()
    }

    override fun loadTracks(paths: List<String>, startIndex: Int) {
        Timber
            .d("Loading tracks (count): ${paths.size} starting at index: $startIndex")
        if (paths == loadedTracks) {
            Timber
                .d("Tracks are already loaded, skipping load.")
            return
        }

        clearTracks()
        val mediaItems = paths.map { path ->
            MediaItem.fromUri(path)
        }
        player.setMediaItems(mediaItems, startIndex, C.TIME_UNSET)
        player.prepare()
        loadedTracks.clear()
        loadedTracks.addAll(paths)
    }

    override fun getLoadedTracks(): List<MediaStore.Audio.Media> {
        Timber.d("Getting loaded tracks")
        return emptyList()
    }

    override fun shuffle(enable: Boolean) {
        Timber.d("Setting shuffle mode to: $enable")
        player.shuffleModeEnabled = enable
    }

    override fun changeRepeatMode(repeatMode: RepeatMode) {
        Timber.d("Changing repeat mode to: $repeatMode")
        player.repeatMode = when (repeatMode) {
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.NONE -> Player.REPEAT_MODE_OFF
        }
    }

    override fun release() {
        Timber.d("Releasing ExoPlayer resources")
        player.release()
    }
}