package com.example.anew.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anew.addScreen.domain.Notes
import com.example.anew.mainscreen.viewmodel.MainVM

@Composable
@Preview(showBackground = true)
fun PreviewMainScreen() {
    // MainScreen(){}
}

@Composable
fun MainScreen(
    mainVM: MainVM = hiltViewModel(),
    deleteNotes: (Notes) -> Unit,
    updateNotes: (Notes) -> Unit,
    routeNavigation: () -> Unit,
) {
    val notesList by mainVM.listNotes.collectAsState(initial = emptyList())

    if (notesList.isEmpty()) {
        Text(text = "У вас ещё нет заметок")
    } else {
        LazyColumn {
            items(notesList) {
                NotesItem(
                    it,
                    deleteNotes = {
                        deleteNotes(it)
                    },
                    updateNotes = { updateNotes(it) }
                ) {
                    routeNavigation()
                }

            }
        }
    }


}