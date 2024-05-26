package ch.walica.todo_repeat_2.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "firstTime")
    val firstTime: Boolean = true,

    @ColumnInfo(name = "active")
    val active: Boolean = true,

    @ColumnInfo(name = "selected")
    val selected: Boolean = false,

    @ColumnInfo(name = "date")
    val date: Long,


    )