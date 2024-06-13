package ch.walica.todo_repeat_2.presentation.delaylist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.ZonedDateTime


@Composable
fun DelayListScreen(delayListViewModel: DelayListViewModel = hiltViewModel()) {

    val delayListState =
        delayListViewModel.state.collectAsState(initial = DelayedListState()).value

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Skills to exercise")
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(
                    items = delayListState.tasks,
                    key = { _, task -> task.id }) { index, task ->
                    ListItem(
                        headlineContent = { Text(text = task.title) },
                        trailingContent = {
                            IconButton(onClick = {
                                delayListViewModel.onEvent(
                                    DelayedListEvent.UpdateTask(
                                        task.copy(
                                            active = true,
                                            date = ZonedDateTime.now().toEpochSecond()
                                        )
                                    )
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Remove,
                                    contentDescription = "To archive"
                                )
                            }
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                    if (index != delayListState.tasks.size - 1) {
                        Divider()
                    }
                }
            }
        }
    }
}