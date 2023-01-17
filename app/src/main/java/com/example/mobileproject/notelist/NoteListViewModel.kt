package com.example.mobileproject.notelist

import android.app.Application
import androidx.lifecycle.*
import com.example.mobileproject.database.Note
import com.example.mobileproject.database.NoteDatabaseDao
import com.example.mobileproject.internetdata.WeatherApi
import kotlinx.coroutines.launch

class NoteListViewModel(dataSource: NoteDatabaseDao): ViewModel() {

    val database = dataSource
    val notes = database.getAllNotes()

    private val _addNewButtonClicked = MutableLiveData<Boolean?>()
    val addNewButtonClicked: LiveData<Boolean?>
    get() = _addNewButtonClicked

    private val _navigateToEditNote = MutableLiveData<Long?>()
    val navigateToEditNote: LiveData<Long?>
    get() = _navigateToEditNote

    private val _showDeleteAllToast = MutableLiveData<Boolean?>()
    val showDeleteAllToast: LiveData<Boolean?>
    get() = _showDeleteAllToast

    val deleteAllButtonVisible = Transformations.map(notes) {
        it.isNotEmpty()
    }

    private val _weatherReport = MutableLiveData<String>()
    val weatherReport: LiveData<String>
    get() = _weatherReport

    init {
        getWeatherReport()
    }

    private fun getWeatherReport() {
        viewModelScope.launch {
            try {
                val result = WeatherApi.retrofitService.getWeatherInfo()
                _weatherReport.value = "Current weather in Debrecen is ${result.current.condition.text}, and the temperature is ${result.current.temp_c} Celsius"
            } catch (e: Exception) {
                _weatherReport.value = "Failed: ${e.message}"
            }
        }
    }

    fun onAddNewButtonClicked() {
        _addNewButtonClicked.value = true
    }

    fun doneNavigatingToAddNote() {
        _addNewButtonClicked.value = null
    }

    fun onEditingNote(id: Long) {
        viewModelScope.launch {
            val note = getNote(id)
            update(note)
        }
    }

    fun onClickEditNote(id: Long) {
        _navigateToEditNote.value = id
    }

    fun doneNavigatingToEditNote() {
        _navigateToEditNote.value =null
    }

    fun onDeleteAllClicked() {
        viewModelScope.launch {
            delete()
            _showDeleteAllToast.value = true
        }
    }

    fun doneShowingDeleteAllToast() {
        _showDeleteAllToast.value = null
    }

    private suspend fun delete() {
        database.deleteAll()
    }

    private suspend fun getNote(id: Long): Note {
        return database.getById(id)
    }

    private suspend fun update(note: Note) {
        database.update(note)
    }
}