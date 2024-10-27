package com.example.anew.addScreen.domain.usecases


data class NotesUseCases(
    val addNotesUC: AddNotesUC,
    val getAllNotesUC: GetAllNotesUC,
    val deleteNotesUC: DeleteNotesUC,
    val getNotes: GetNotesUC,
)