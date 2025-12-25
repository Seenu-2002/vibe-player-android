package com.seenu.dev.android.vibeplayer.domain.model

data class ScanConfig constructor(
    val minSize: MinSize = MinSize.KB_100,
    val minDuration: MinDuration = MinDuration.SEC_30,
    val isInitialScanDone: Boolean = false
) {
    enum class MinSize constructor(val value: Long) {
        KB_100(100),
        KB_500(500),
    }

    enum class MinDuration constructor(val value: Long) {
        SEC_30(30),
        SEC_60(60),
    }
}