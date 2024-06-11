package ch.walica.todo_repeat_2.domain.usecase

import ch.walica.todo_repeat_2.data.database.TaskDao
import ch.walica.todo_repeat_2.domain.model.Task
import javax.inject.Inject

class UpsertTaskUseCase @Inject constructor(private val taskDao: TaskDao) {

    suspend operator fun invoke(task: Task) {
        taskDao.upsertTask(task)
    }
}