package ch.walica.todo_repeat_2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ch.walica.todo_repeat_2.domain.model.Task

@Database(entities = [Task::class], version = 1)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao
}