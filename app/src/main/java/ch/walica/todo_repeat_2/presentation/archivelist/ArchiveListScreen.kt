package ch.walica.todo_repeat_2.presentation.archivelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
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
import ch.walica.todo_repeat_2.presentation.delaylist.DelayedListEvent
import java.time.ZonedDateTime

@Composable
fun ArchiveListScreen(archiveListViewModel: ArchiveListViewModel = hiltViewModel()) {

    val archivedListState =
        archiveListViewModel.state.collectAsState(initial = ArchivedListState()).value

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
                    items = archivedListState.tasks,
                    key = { _, task -> task.id }) { index, task ->
                    ListItem(
                        headlineContent = { Text(text = task.title) },
                        trailingContent = {
                            Row {
                                IconButton(onClick = {
                                    archiveListViewModel.onEvent(
                                        ArchivedListEvent.UpdateTask(
                                            task.copy(
                                                active = true,
                                                archived = false,
                                                date = ZonedDateTime.now().toEpochSecond()
                                            )
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = "To Active tasks"
                                    )
                                }

                                IconButton(onClick = {
                                    archiveListViewModel.onEvent(
                                        ArchivedListEvent.DeleteTask(task)
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            }

                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                    if (index != archivedListState.tasks.size - 1) {
                        Divider()
                    }
                }
            }
        }
    }
}