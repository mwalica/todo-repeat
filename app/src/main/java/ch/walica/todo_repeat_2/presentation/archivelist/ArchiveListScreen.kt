package ch.walica.todo_repeat_2.presentation.archivelist

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import ch.walica.todo_repeat_2.R
import ch.walica.todo_repeat_2.presentation.common.components.DescScreenText
import ch.walica.todo_repeat_2.presentation.ui.theme.DarkGray
import ch.walica.todo_repeat_2.presentation.ui.theme.SurfaceVariantLight
import java.time.ZonedDateTime

@Composable
fun ArchiveListScreen(archiveListViewModel: ArchiveListViewModel = hiltViewModel()) {

    val archivedListState =
        archiveListViewModel.state.collectAsState(initial = ArchivedListState()).value

    Column(modifier = Modifier.fillMaxWidth()) {
        DescScreenText(text = stringResource(id = R.string.desc_archive_screen))
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
                        headlineContent = {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = if (isSystemInDarkTheme()) SurfaceVariantLight else DarkGray
                                )
                            )
                        },
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
                                        imageVector = Icons.Rounded.KeyboardDoubleArrowLeft,
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