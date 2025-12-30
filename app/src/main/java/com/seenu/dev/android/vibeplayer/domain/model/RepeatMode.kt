package com.seenu.dev.android.vibeplayer.domain.model

enum class RepeatMode {
    ALL, ONE, NONE;

    fun next(): RepeatMode {
        return when (this) {
            ALL -> ONE
            ONE -> NONE
            NONE -> ALL
        }
    }
}