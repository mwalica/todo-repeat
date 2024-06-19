package ch.walica.todo_repeat_2.presentation.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.walica.todo_repeat_2.presentation.BottomTab
import ch.walica.todo_repeat_2.presentation.Route
import ch.walica.todo_repeat_2.presentation.archivelist.ArchiveListScreen
import ch.walica.todo_repeat_2.presentation.delaylist.DelayListScreen
import ch.walica.todo_repeat_2.presentation.tasklist.TaskListScreen
import ch.walica.todo_repeat_2.presentation.ui.theme.LightGray
import ch.walica.todo_repeat_2.presentation.ui.theme.Primary
import ch.walica.todo_repeat_2.presentation.ui.theme.SurfaceVariantDark
import ch.walica.todo_repeat_2.presentation.ui.theme.SurfaceVariantLight
import ch.walica.todo_repeat_2.presentation.ui.theme.White10
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel()) {

    val state = mainViewModel.state.collectAsState().value

    val navController = rememberNavController()
    val bottomTabs = listOf(BottomTab.Tasks, BottomTab.Delay, BottomTab.Archive)

    val currentRoute = navController.currentBackStackEntryFlow.map { backStackEntry ->
        when (backStackEntry.destination.route) {
            Route.Tasks.routeName -> "Tasks"
            Route.Delay.routeName -> "Delayed tasks"
            Route.Archive.routeName -> "Archive"
            else -> ""
        }
    }.collectAsState(initial = "Tasks")

    var index by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentRoute.value,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    if (currentRoute.value == "Tasks") {
                        IconButton(onClick = {
                            mainViewModel.onEvent(MainEvent.ShowDialog)
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "add task")
                        }
                    }

                },
                colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary.copy(),
                    titleContentColor = Color.White.copy(alpha = 0.7f),
                    actionIconContentColor = Color.White.copy(alpha = 0.7f)
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent
            ) {
                NavigationBar(
                    containerColor = Color.Transparent
                ) {
                    bottomTabs.forEachIndexed { i, bottomTab ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = if (isSystemInDarkTheme()) {
                                    SurfaceVariantDark
                                } else {
                                    SurfaceVariantLight
                                }
                            ),
                            selected = index == i,
                            onClick = {
                                index = i
                                navController.navigate(bottomTab.route.routeName) {
                                    popUpTo(navController.graph.id)
                                }
                            },
                            icon = {
                                if (index == i) {
                                    Icon(
                                        imageVector = bottomTab.selectedIcon,
                                        contentDescription = bottomTab.title
                                    )
                                } else {
                                    Icon(
                                        imageVector = bottomTab.unselectedIcon,
                                        contentDescription = bottomTab.title
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = bottomTab.title
                                )
                            }
                        )
                    }

                }
            }
        }
    ) { padding ->
        MainScreenContent(
            padding = padding,
            navController = navController,
            mainState = state,
            mainViewModel = mainViewModel
        )
    }
}


@Composable
fun MainScreenContent(
    padding: PaddingValues,
    navController: NavHostController,
    mainState: MainState,
    mainViewModel: MainViewModel
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        NavHost(navController = navController, startDestination = Route.Tasks.routeName) {
            composable(route = Route.Tasks.routeName) {
                TaskListScreen(
                    mainState = mainState,
                    hideDialog = { mainViewModel.onEvent(MainEvent.HideDialog) })
            }

            composable(route = Route.Delay.routeName) {
                DelayListScreen()
            }

            composable(route = Route.Archive.routeName) {
                ArchiveListScreen()
            }
        }
    }

}