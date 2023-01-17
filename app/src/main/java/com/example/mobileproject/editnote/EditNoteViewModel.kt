package com.example.mobileproject.editnote

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileproject.convertCreationDateToStringWithLabel
import com.example.mobileproject.database.Note
import com.example.mobileproject.database.NoteDatabaseDao
import kotlinx.coroutines.launch

class EditNoteViewModel(
    dataSource: NoteDatabaseDao,
    application: Application,
    private val id: Long): ViewModel() {

        val database = dataSource

    private val _note = MutableLiveData<Note?>()
    val note: LiveData<Note?>
    get() = _note

    private val _backButtonClicked = MutableLiveData<Boolean?>()
    val backButtonClicked: LiveData<Boolean?>
    get() = _backButtonClicked

    private val _editButtonClicked = MutableLiveData<Boolean?>()
    val editButtonClicked: LiveData<Boolean?>
    get() = _editButtonClicked

    private val _deleteButtonClicked = MutableLiveData<Boolean?>()
    val deleteButtonClicked: LiveData<Boolean?>
    get() = _deleteButtonClicked

    val noteCreationDateFormatted = Transformations.map(note) { note ->
        note?.let {
            convertCreationDateToStringWithLabel(it.timeCreated, "Note was created at:")
        }
    }

    init {
        getNoteById()
    }

    private fun getNoteById() {
        viewModelScope.launch {
            _note.value = getNote()
        }
    }

    private suspend fun getNote(): Note {
        return database.getById(id)
    }

    fun onClickBackButton() {
        _backButtonClicked.value = true
    }

    fun doneNavigatingBack() {
        _backButtonClicked.value = null
    }

    fun onClickEditButton() {
        if (_editButtonClicked.value == null) {
            _editButtonClicked.value = true
        } else if (_editButtonClicked.value == true) {
            _editButtonClicked.value = false
        }
    }

    fun doneEditingNote(newTitle: String, newDescription: String) {
        viewModelScope.launch {
            update(newTitle, newDescription)
            _editButtonClicked.value = null
        }
    }

    private suspend fun update(newTitle: String, newDescription: String) {
        _note.value?.let {
            val newNote = Note(it.id, newTitle, newDescription)
            _note.value = newNote
            database.update(newNote)
        }
    }

    fun onClickDeleteButton() {
        viewModelScope.launch {
            delete()
            _deleteButtonClicked.value = true
            _backButtonClicked.value = true
        }
    }

    fun doneDeletingNote() {
        _deleteButtonClicked.value = null
    }

    private suspend fun delete() {
        database.deleteById(id)
    }
}