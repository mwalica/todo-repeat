package ch.walica.todo_repeat_2.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.walica.todo_repeat_2.domain.model.Task
import ch.walica.todo_repeat_2.domain.usecase.GetActiveTasksUseCase
import ch.walica.todo_repeat_2.domain.usecase.UpsertTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val upsertTaskUseCase: UpsertTaskUseCase,
    getActiveTasksUseCase: GetActiveTasksUseCase
) : ViewModel() {

    private val _tasks = getActiveTasksUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val _state = MutableStateFlow(TaskListState())
    val state = combine(_state, _tasks) { state, tasks ->
        state.copy(
            tasks = tasks
        )
    }

    fun onEvent(event: TaskListEvent) {
        when (event) {
            is TaskListEvent.SetTask -> {
                _state.update { state ->
                    state.copy(
                        title = event.task
                    )
                }
            }

            TaskListEvent.SaveTask -> {
                val title = _state.value.title
                val date = ZonedDateTime.now()

                if (title.isBlank()) return

                val task = Task(
                    title = title,
                    date = date.toEpochSecond(),
                )

                viewModelScope.launch {
                    upsertTaskUseCase(task = task)
                }
            }

            is TaskListEvent.UpdateTask -> {
                val task = event.task
                viewModelScope.launch {
                    upsertTaskUseCase(task = task)
                }
            }

        }
    }


}

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val title: String = "",
    val isAddingTask: Boolean = false
)

sealed interface TaskListEvent {
    data class SetTask(val task: String) : TaskListEvent
    data object SaveTask : TaskListEvent
    data class UpdateTask(val task: Task) : TaskListEvent
}