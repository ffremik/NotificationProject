package com.example.anew.addScreen.domain


data class Notes(
    val id: String,
    val title: String,
    val textNotes: String,
    val requestCode: Int? = null,
    val informationClock: String? = null
)