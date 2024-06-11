package ch.walica.todo_repeat_2.presentation.tasklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.walica.todo_repeat_2.R
import ch.walica.todo_repeat_2.presentation.main.MainState

@Composable
fun TaskListScreen(
    taskListViewModel: TaskListViewModel = hiltViewModel(),
    mainState: MainState,
    hideDialog: () -> Unit
) {

    val taskListState = taskListViewModel.state.collectAsState(TaskListState()).value

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(items = taskListState.tasks, key = { _, todo -> todo.id }) { index, todo ->
            Text(text = todo.title)
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