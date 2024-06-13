package ch.walica.todo_repeat_2.presentation.archivelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.walica.todo_repeat_2.domain.model.Task
import ch.walica.todo_repeat_2.domain.usecase.DeleteTaskUseCase
import ch.walica.todo_repeat_2.domain.usecase.GetArchivedTasksUseCase
import ch.walica.todo_repeat_2.domain.usecase.UpsertTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArchiveListViewModel @Inject constructor(
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getArchivedTasksUseCase: GetArchivedTasksUseCase
) : ViewModel() {
    private val _tasks = getArchivedTasksUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val _state = MutableStateFlow(ArchivedListState())
    val state = combine(_state, _tasks) { state, tasks ->
        state.copy(
            tasks = tasks
        )
    }

    fun onEvent(event: ArchivedListEvent) {
        when (event) {
            is ArchivedListEvent.UpdateTask -> {
                viewModelScope.launch {
                    upsertTaskUseCase(task = event.task)
                }
            }

            is ArchivedListEvent.DeleteTask -> {
                viewModelScope.launch {
                    deleteTaskUseCase(task = event.task)
                }
            }
        }
    }


}

data class ArchivedListState(
    val tasks: List<Task> = emptyList(),
)

sealed interface ArchivedListEvent {
    data class UpdateTask(val task: Task) : ArchivedListEvent
    data class DeleteTask(val task: Task) : ArchivedListEvent
}