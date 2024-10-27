package com.example.anew.addScreen

import TimPicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anew.addScreen.viewmodel.AddNotesViewModel

@Preview(showBackground = true)
@Composable
fun PreviewAddNotesScreen() {
    //AddNotesScreen()
}

@Composable
fun AddNotesScreen(
    addNotesViewModel: AddNotesViewModel
) {
    val textTitle = addNotesViewModel.textTitle.collectAsState()
    val textNote = addNotesViewModel.textNotes.collectAsState()
    val isAddNotification by addNotesViewModel.isAddNotification.collectAsState()
    val hours by addNotesViewModel.hours.collectAsState()
    val minutes by addNotesViewModel.minutes.collectAsState()

    val notes by addNotesViewModel.notesEditing.collectAsState()
    val informationClock by addNotesViewModel.informationClockNotes.collectAsState()

    val hourState = rememberLazyListState()
    val minuteState = rememberLazyListState()
    val stateScroll = rememberScrollState()
    val isRepeatNotification by addNotesViewModel.repeatNotification.collectAsState()

    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateScroll),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(text = "$hours $minutes")
            Box(
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = textTitle.value,
                    onValueChange = {
                        addNotesViewModel.updateTextTitle(it)
                    },

                    maxLines = 1,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 21.sp,
                        textAlign = TextAlign.Center
                    ),
                )
                if (textTitle.value.isEmpty()) {
                    Text(
                        text = "Заголовок",
                        fontSize = 21.sp
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                onClick = { }
            ) {
                Box(
                    contentAlignment = Alignment.TopStart
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(minHeight = 160.dp),
                        value = textNote.value,
                        onValueChange = {
                            addNotesViewModel.updateTextNotes(it)
                        }
                    )
                    if (textNote.value.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(top = 20.dp, start = 16.dp),
                            text = "Начните вводить"
                        )
                    }
                }

            }

            if (informationClock != null) {
                Text(text = "Напоминания:")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "${informationClock}")
                    Icon(
                        modifier = Modifier.clickable {
                            addNotesViewModel.deleteNotificationNotes()
                        },
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Notification"
                    )
                }

            } else {
                Text(
                    text = "Настройки: "
                )
                Icon(
                    modifier = Modifier.clickable {
                        addNotesViewModel.updateIsAddNotification()
                    },
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Notification"
                )
                if (isAddNotification) {
                    TimPicker(
                        hourState = hourState,
                        minuteState = minuteState
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Повторять? ")
                        Checkbox(
                            checked = isRepeatNotification,
                            onCheckedChange = {
                                addNotesViewModel.updateIsRepeatNotification()
                            }
                        )
                    }
                }

            }
            Button(
                modifier = Modifier.padding(top = 12.dp),
                onClick = {
                    addNotesViewModel.updateTime(hourState.firstVisibleItemIndex, minuteState.firstVisibleItemIndex)
                    addNotesViewModel.addNotes()
                }
            ) {
                Text(text = "Сохранить")
            }
        }

    }
}
