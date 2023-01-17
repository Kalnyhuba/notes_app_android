package com.example.mobileproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDatabaseDao {

    @Insert
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Query("DELETE FROM notes WHERE id = :key")
    suspend fun deleteById(key: Long)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes WHERE id = :key")
    suspend fun getById(key: Long): Note

    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Note>>
}