package ch.walica.todo_repeat_2.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ch.walica.todo_repeat_2.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE active = 1 ORDER BY date DESC ")
    fun getActiveTasks(): Flow<List<Task>>
}