package com.example.mobileproject.editnote

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobileproject.database.NoteDatabaseDao

class EditNoteViewModelFactory(
    private val dataSource: NoteDatabaseDao,
    private val application: Application,
    private val id: Long): ViewModelProvider.Factory {

    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNoteViewModel::class.java)) {
            return EditNoteViewModel(dataSource, application, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}