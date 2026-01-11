package com.seenu.dev.android.vibeplayer.domain.exceptions

class PlaylistAlreadyExistException constructor(name: String) : Exception(
    "Playlist with name '$name' already exists."
)