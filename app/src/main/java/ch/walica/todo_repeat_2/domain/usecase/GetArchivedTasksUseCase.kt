package ch.walica.todo_repeat_2.domain.usecase

import ch.walica.todo_repeat_2.data.database.TaskDao
import ch.walica.todo_repeat_2.domain.model.Task
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetArchivedTasksUseCase @Inject constructor(private val taskDao: TaskDao) {

    operator fun invoke(): Flow<List<Task>> {
        return taskDao.getArchivedTasks()
    }

}