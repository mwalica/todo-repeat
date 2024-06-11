package ch.walica.todo_repeat_2.presentation.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.ShowDialog -> _state.update { state ->
                state.copy(
                    isShow = true
                )
            }

            MainEvent.HideDialog -> _state.update { state ->
                state.copy(
                    isShow = false
                )
            }
        }
    }


}

data class MainState(
    val isShow: Boolean = false
)

sealed interface MainEvent {
    data object ShowDialog : MainEvent
    data object HideDialog : MainEvent
}
