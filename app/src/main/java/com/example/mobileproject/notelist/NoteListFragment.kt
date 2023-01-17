package com.example.mobileproject.notelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobileproject.R
import com.example.mobileproject.database.NoteDatabase
import com.example.mobileproject.databinding.FragmentNoteListBinding

class NoteListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentNoteListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_list, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory = NoteListViewModelFactory(dataSource)
        val noteListViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(NoteListViewModel::class.java)
        binding.noteListViewModel = noteListViewModel
        binding.lifecycleOwner = this

        val adapter = NoteAdapter(NoteListener { id ->
            noteListViewModel.onEditingNote(id)
        }, NoteListener { id ->
            noteListViewModel.onClickEditNote(id)
        })
        binding.allNotesRecyclerView.adapter = adapter

        noteListViewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        noteListViewModel.addNewButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    NoteListFragmentDirections.actionFragmentNoteListToFragmentAddNote()
                )
                noteListViewModel.doneNavigatingToAddNote()
            }
        })

        noteListViewModel.navigateToEditNote.observe(viewLifecycleOwner, Observer { id ->
            id?.let {
                this.findNavController().navigate(
                    NoteListFragmentDirections.actionFragmentNoteListToFragmentEditNote(id)
                )
                noteListViewModel.doneNavigatingToEditNote()
            }
        })

        noteListViewModel.showDeleteAllToast.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, "Successfully deleted all notes", Toast.LENGTH_SHORT).show()
                noteListViewModel.doneShowingDeleteAllToast()
            }
        })

        noteListViewModel.deleteAllButtonVisible.observe(viewLifecycleOwner, Observer {
            binding.deleteAllNotesButton.isEnabled = it
            if (it == true) {
                binding.emptyListLabel.visibility = View.GONE
            } else {
                binding.emptyListLabel.visibility = View.VISIBLE
            }
        })

        return binding.root
    }
}