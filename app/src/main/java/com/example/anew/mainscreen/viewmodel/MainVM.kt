package com.example.anew.mainscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.anew.addScreen.domain.Notes
import com.example.anew.addScreen.domain.usecases.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val notesUC: NotesUseCases
) : ViewModel(){
    val listNotes = notesUC.getAllNotesUC.invoke()


}