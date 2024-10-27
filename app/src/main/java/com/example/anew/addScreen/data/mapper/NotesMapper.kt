package com.example.anew.addScreen.data.mapper

import com.example.anew.addScreen.data.database.NotesEntity
import com.example.anew.addScreen.domain.Notes

fun Notes.toNotesEntity(): NotesEntity {
    return NotesEntity(
        id = this.id,
        title = this.title,
        textNotes = this.textNotes,
        requestCode = this.requestCode,
        informationClock = this.informationClock
    )
}