package com.example.mobileproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "title")
    var noteTitle: String = "",

    @ColumnInfo(name = "description")
    var noteDescription: String = "",

    @ColumnInfo(name = "timeCreated")
    var timeCreated: Long = System.currentTimeMillis()
) {

    constructor(id: Long, noteTitle: String, noteDescription: String): this() {
        this.id = id
        this.noteTitle = noteTitle
        this.noteDescription = noteDescription
    }
}

