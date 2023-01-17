package com.example.mobileproject.addnote

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileproject.database.Note
import com.example.mobileproject.database.NoteDatabaseDao
import kotlinx.coroutines.launch

class AddNoteViewModel(dataSource: NoteDatabaseDao, application: Application): ViewModel() {

    val database = dataSource

    private val _backButtonClicked = MutableLiveData<Boolean?>()
    val backButtonClicked: LiveData<Boolean?>
    get() = _backButtonClicked

    private val _addButtonClicked = MutableLiveData<Boolean?>()
    val addButtonClicked: LiveData<Boolean?>
    get() = _addButtonClicked

    fun onCreateNote(title: String, description: String) {
        viewModelScope.launch {
            val newNote = Note()
            if (title != "" && description != "") {
                newNote.noteTitle = title
                newNote.noteDescription = description
            } else {
                if (title == "") {
                    newNote.noteTitle = "Empty title"
                }
                if (description == "") {
                    newNote.noteDescription = "Empty description"
                }
            }
            insert(newNote)
            _backButtonClicked.value = true
        }
    }

    private suspend fun insert(note: Note) {
        database.insert(note)
    }

    fun onClickBackButton() {
        _backButtonClicked.value = true
    }

    fun doneNavigatingBack() {
        _backButtonClicked.value = null
    }

    fun onClickAddButton() {
        _addButtonClicked.value = true
    }

    fun doneNavigatingForth() {
        _addButtonClicked.value = null
    }
}