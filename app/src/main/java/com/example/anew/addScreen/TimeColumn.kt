import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun TimPicker(
    hourState: LazyListState,
    minuteState: LazyListState
) {
    val hours = (0..23).toList()
    val minutes = (0..59).toList()


    Column(

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Часы")
                TimeColumn(
                    items = hours,
                    state = hourState,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Минуты")
                TimeColumn(
                    items = minutes,
                    state = minuteState,
                )
            }

        }

    }
}

@Composable
fun TimeColumn(
    items: List<Int>,
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val visibleItemIndex by remember {
        derivedStateOf {
            state.firstVisibleItemIndex
        }
    }

    LazyColumn(
        state = state,
        modifier = modifier.height(150.dp),
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(vertical = 32.dp)
    ) {
        items(items) { item ->
            val isSelected = items.indexOf(item) == visibleItemIndex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.toString().padStart(2, '0'),
                    fontSize = if (isSelected) 36.sp else 24.sp,
                    color = if (isSelected) Color.Cyan else Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
        item { 
            Spacer(modifier = Modifier.height(45.dp))
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            state.animateScrollToItem(visibleItemIndex)
        }
    }

}
