package ch.walica.todo_repeat_2.presentation.delaylist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import ch.walica.todo_repeat_2.R
import ch.walica.todo_repeat_2.presentation.common.components.DescScreenText
import java.time.ZonedDateTime


@Composable
fun DelayListScreen(delayListViewModel: DelayListViewModel = hiltViewModel()) {

    val delayListState =
        delayListViewModel.state.collectAsState(initial = DelayedListState()).value


    Column(modifier = Modifier.fillMaxWidth()) {
        DescScreenText(text = stringResource(id = R.string.desc_delay_screen))
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(
                    items = delayListState.tasks,
                    key = { _, task -> task.id }) { index, task ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        },
                        trailingContent = {
                            Row {
                                IconButton(onClick = {
                                    delayListViewModel.onEvent(
                                        DelayedListEvent.UpdateTask(
                                            task.copy(
                                                active = true,
                                                date = ZonedDateTime.now().toEpochSecond()
                                            )
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.ChevronLeft,
                                        contentDescription = "To Active tasks"
                                    )
                                }
                                IconButton(onClick = {
                                    delayListViewModel.onEvent(
                                        DelayedListEvent.UpdateTask(
                                            task.copy(
                                                active = false,
                                                archived = true
                                            )
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.ChevronRight,
                                        contentDescription = "To Archive tasks"
                                    )
                                }
                            }

                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                    if (index != delayListState.tasks.size - 1) {
                        Divider()
                    }
                }
            }
        }
    }
}