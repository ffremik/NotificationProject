package com.example.anew.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.anew.addScreen.domain.Notes

@Preview(showBackground = true)
@Composable
fun PreviewNotesItem(){
    //NotesItem(Notes("Dsa", "seaw","dse")){}
}

@Composable
fun NotesItem(
    notes: Notes,
    updateNotes: (Notes) -> Unit,
    deleteNotes: (Notes) -> Unit,
    routeNavigation: () -> Unit,
){
    Card(
        modifier = Modifier
            .padding(4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        updateNotes(notes)
                        routeNavigation()
                    }
                )
            }

    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    text = notes.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = notes.textNotes,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = if (notes.informationClock != null) "${notes.informationClock}" else "",
                    fontSize = 10.sp
                )
            }
            Icon(
                modifier = Modifier
                    .padding(top = 4.dp, end = 4.dp)
                    .clickable {
                        deleteNotes(notes)
                    },
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Notes"
            )
        }
    }
}