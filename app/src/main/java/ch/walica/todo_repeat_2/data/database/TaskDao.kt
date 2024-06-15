package ch.walica.todo_repeat_2.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ch.walica.todo_repeat_2.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime


@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE active = 1 OR (archived = 0 AND date < :date) ORDER BY date DESC ")
    fun getActiveTasks(date: Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE active = 0 AND archived = 0 AND date >= :date ORDER BY date DESC ")
    fun getDelayedTasks(date: Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE active = 0 AND archived = 1 ORDER BY date DESC ")
    fun getArchivedTasks(): Flow<List<Task>>
}