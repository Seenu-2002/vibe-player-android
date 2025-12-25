package com.seenu.dev.android.vibeplayer.data.source

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.seenu.dev.android.vibeplayer.data.model.MusicTrackEntity
import org.koin.core.annotation.Single

@Single
class LocalMusicScanner constructor(
    private val context: Context
) {

    suspend fun scanMusic(
        minFileSizeInBytes: Long,
        minDurationMs: Long
    ): List<MusicTrackEntity> {
        val collections = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.TRACK,
        )
        val selection = """
            ${MediaStore.Audio.Media.IS_MUSIC} = 1
            AND ${MediaStore.Audio.Media.DURATION} >= ?
            AND ${MediaStore.Audio.Media.SIZE} >= ?
            """.trimIndent()

        val selectionArgs = arrayOf(
            minDurationMs.toString(),
            minFileSizeInBytes.toString()
        )
        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        val query = context.contentResolver.query(
            collections,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        val musicList = mutableListOf<MusicTrackEntity>()

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val path = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumId = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            var i = 1L
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val album = cursor.getString(albumColumn)
                val artist = cursor.getString(artistColumn)
                val duration = cursor.getLong(durationColumn)
                val size = cursor.getLong(sizeColumn)
                val dataPath = cursor.getString(path)
                val albumId = cursor.getLong(albumId)
                musicList.add(
                    MusicTrackEntity(
                        musicId = id,
                        name = name,
                        artist = artist,
                        albumId = albumId,
                        album = album,
                        size = size,
                        path = dataPath,
                        duration = duration,
                        id = i++
                    )
                )
            }
        }
        return musicList
    }

}