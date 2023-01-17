package com.example.mobileproject.notelist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobileproject.database.NoteDatabaseDao

class NoteListViewModelFactory(
    private val dataSource: NoteDatabaseDao):
    ViewModelProvider.Factory {

    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            return NoteListViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}