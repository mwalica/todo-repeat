package ch.walica.todo_repeat_2.presentation.delaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.walica.todo_repeat_2.domain.model.Task
import ch.walica.todo_repeat_2.domain.usecase.GetDelayedUseCase
import ch.walica.todo_repeat_2.domain.usecase.UpsertTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DelayListViewModel @Inject constructor(
    private val upsertTaskUseCase: UpsertTaskUseCase,
    getDelayedUseCase: GetDelayedUseCase
) : ViewModel() {

    private val _tasks = getDelayedUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val _state = MutableStateFlow(DelayedListState())
    val state = combine(_state, _tasks) { state, tasks ->
        state.copy(
            tasks = tasks
        )
    }

    fun onEvent(event: DelayedListEvent) {
        when(event) {
            is DelayedListEvent.UpdateTask -> {
                viewModelScope.launch {
                    val task = event.task
                    viewModelScope.launch {
                        upsertTaskUseCase(task = task)
                    }
                }
            }
        }
    }
}

data class DelayedListState(
    val tasks: List<Task> = emptyList(),
)

sealed interface DelayedListEvent {
    data class UpdateTask(val task: Task) : DelayedListEvent
}
