package com.seenu.dev.android.vibeplayer.presentation.utils

fun<T> List<T>.findWithIndex(predicate: (T) -> Boolean): Pair<Int, T>? {
    for (index in indices) {
        val item = this[index]
        if (predicate(item)) {
            return Pair(index, item)
        }
    }

    return null
}