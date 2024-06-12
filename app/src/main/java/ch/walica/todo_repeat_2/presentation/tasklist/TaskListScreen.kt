package ch.walica.todo_repeat_2.presentation.tasklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ch.walica.todo_repeat_2.R
import ch.walica.todo_repeat_2.presentation.main.MainState
import java.time.ZonedDateTime

@Composable
fun TaskListScreen(
    taskListViewModel: TaskListViewModel = hiltViewModel(),
    mainState: MainState,
    hideDialog: () -> Unit
) {

    val taskListState = taskListViewModel.state.collectAsState(TaskListState()).value

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
                    items = taskListState.tasks,
                    key = { _, task -> task.id }) { index, task ->
                    ListItem(
                        headlineContent = { Text(text = task.title) },
                        trailingContent = {
                            IconButton(onClick = {
                                taskListViewModel.onEvent(
                                    TaskListEvent.UpdateTask(
                                        task.copy(
                                            active = false,
                                            firstTime = false,
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
                    if (index != taskListState.tasks.size - 1) {
                        Divider()
                    }
                }
            }
        }
    }



    if (mainState.isShow) {
        AlertDialog(
            onDismissRequest = hideDialog,
            confirmButton = {
                OutlinedButton(onClick = {
                    taskListViewModel.onEvent(TaskListEvent.SaveTask)
                    hideDialog()
                }) {
                    Text(text = "Add task")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = hideDialog) {
                    Text(text = "Cancel")
                }
            },
            title = { Text(text = "New Task") },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
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