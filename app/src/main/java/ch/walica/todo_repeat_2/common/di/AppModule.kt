package ch.walica.todo_repeat_2.common.di

import android.app.Application
import androidx.room.Room
import ch.walica.todo_repeat_2.data.database.MyRoomDatabase
import ch.walica.todo_repeat_2.data.database.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun myRoomDatabase(application: Application): MyRoomDatabase {
        return Room.databaseBuilder(
            application,
            MyRoomDatabase::class.java,
            "tasks_db"
        ).build()
    }

    @Provides
    fun taskDao(database: MyRoomDatabase): TaskDao {
        return database.taskDao
    }
}