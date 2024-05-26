package ch.walica.todo_repeat_2.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Task
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Route
) {
    data object Tasks : BottomTab(
        title = "Tasks",
        selectedIcon = Icons.Filled.Task,
        unselectedIcon = Icons.Outlined.Task,
        route = Route.Tasks
    )

    data object Delay : BottomTab(
        title = "Delay",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        route = Route.Delay
    )

    data object Archive : BottomTab(
        title = "Archive",
        selectedIcon = Icons.Filled.Archive,
        unselectedIcon = Icons.Outlined.Archive,
        route = Route.Archive
    )
}

