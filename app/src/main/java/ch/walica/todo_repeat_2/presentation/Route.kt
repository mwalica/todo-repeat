package ch.walica.todo_repeat_2.presentation

sealed class Route(val routeName: String) {
    data object Tasks : Route(routeName = "tasks")
    data object Delay : Route(routeName = "delay")
    data object Archive : Route(routeName = "archive")
}
