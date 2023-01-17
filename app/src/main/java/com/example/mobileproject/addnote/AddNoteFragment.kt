package com.example.mobileproject.addnote

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobileproject.R
import com.example.mobileproject.database.NoteDatabase
import com.example.mobileproject.databinding.FragmentAddNoteBinding

class AddNoteFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentAddNoteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_note, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory = AddNoteViewModelFactory(dataSource, application)
        val addNoteViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(AddNoteViewModel::class.java)
        binding.addNoteViewModel = addNoteViewModel
        binding.lifecycleOwner = this

        addNoteViewModel.backButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddNoteFragmentDirections.actionFragmentAddNoteToFragmentNoteList()
                )
                addNoteViewModel.doneNavigatingBack()
            }
        })

        addNoteViewModel.addButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
                val title = binding.editTextTitle.text.toString()
                val description = binding.editTextDescription.text.toString()
                addNoteViewModel.onCreateNote(title, description)
                val manager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(this.view?.windowToken, 0)
                addNoteViewModel.doneNavigatingForth()
            }
        })
        return binding.root
    }
}