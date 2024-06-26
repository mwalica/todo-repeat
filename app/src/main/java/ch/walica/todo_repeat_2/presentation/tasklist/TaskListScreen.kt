package ch.walica.todo_repeat_2.presentation.tasklist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.walica.todo_repeat_2.R
import ch.walica.todo_repeat_2.presentation.common.components.DescScreenText
import ch.walica.todo_repeat_2.presentation.main.MainState
import ch.walica.todo_repeat_2.presentation.ui.theme.DarkGray
import ch.walica.todo_repeat_2.presentation.ui.theme.LightGray
import ch.walica.todo_repeat_2.presentation.ui.theme.PurpleGrey40
import ch.walica.todo_repeat_2.presentation.ui.theme.SurfaceVariantDark
import ch.walica.todo_repeat_2.presentation.ui.theme.SurfaceVariantLight
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun TaskListScreen(
    taskListViewModel: TaskListViewModel = hiltViewModel(),
    mainState: MainState,
    hideDialog: () -> Unit
) {

    val taskListState = taskListViewModel.state.collectAsState(TaskListState()).value

    val dayOfYear = ZonedDateTime.now().dayOfYear
    taskListState.tasks.filter { task ->
        task.selected && ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(task.date),
            ZoneId.systemDefault()
        ).dayOfYear != dayOfYear
    }.forEach { task ->
        taskListViewModel.onEvent(
            TaskListEvent.UpdateTask(
                task.copy(
                    selected = false
                )
            )
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        DescScreenText(text = stringResource(R.string.desc_tasks_screen))
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(
                    items = taskListState.tasks,
                    key = { _, task -> task.id }) { index, task ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = task.title,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .clickable {
                                        taskListViewModel.onEvent(
                                            TaskListEvent.UpdateTask(
                                                task.copy(
                                                    date = ZonedDateTime
                                                        .now()
                                                        .toEpochSecond(),
                                                    selected = !task.selected,
                                                    active = true
                                                )
                                            )
                                        )
                                    },
                                style = if (isSystemInDarkTheme()) {
                                    MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = if (task.selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (task.selected) MaterialTheme.colorScheme.primary else SurfaceVariantLight

                                    )
                                } else {
                                    MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = if (task.selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (task.selected) MaterialTheme.colorScheme.primary else DarkGray

                                    )
                                }
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = {
                                taskListViewModel.onEvent(
                                    TaskListEvent.UpdateTask(
                                        task.copy(
                                            active = false,
                                            firstTime = false,
                                            selected = false,
                                            date = ZonedDateTime.now().toEpochSecond()
                                        )
                                    )
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = "To delayed"
                                )
                            }
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                            trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    if (index != taskListState.tasks.size - 1) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }



    if (mainState.isShow) {
        AlertDialog(
            containerColor = if (isSystemInDarkTheme()) SurfaceVariantDark else LightGray,
            onDismissRequest = hideDialog,
            confirmButton = {
                Button(onClick = {
                    taskListViewModel.onEvent(TaskListEvent.SaveTask)
                    hideDialog()
                }) {
                    Text(text = "Add task")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = hideDialog,

                ) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "New Task") },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        value = taskListState.title,
                        onValueChange = { taskListViewModel.onEvent(TaskListEvent.SetTask(it)) },
                        modifier = Modifier.weight(1f),
                        label = { Text(text = stringResource(R.string.add_task)) }
                    )
                }
            },
        )
    }

}