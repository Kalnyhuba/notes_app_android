package com.example.mobileproject.editnote

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
import com.example.mobileproject.databinding.FragmentEditNoteBinding

class EditNoteFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEditNoteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_note, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = EditNoteFragmentArgs.fromBundle(arguments)
        val dataSource = NoteDatabase.getDatabase(application).noteDatabaseDao
        val viewModelFactory = EditNoteViewModelFactory(dataSource, application, arguments.id)
        val editNoteViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditNoteViewModel::class.java)
        binding.editNoteViewModel = editNoteViewModel
        binding.lifecycleOwner = this

        editNoteViewModel.backButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    EditNoteFragmentDirections.actionFragmentEditNoteToFragmentNoteList()
                )
                editNoteViewModel.doneNavigatingBack()
            }
        })

        editNoteViewModel.editButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.apply {
                    editNoteTitle.visibility = View.GONE
                    editNoteEditTitle.visibility = View.VISIBLE
                    editNoteDescription.visibility = View.GONE
                    editNoteEditDescription.visibility = View.VISIBLE
                    editNoteEditTitle.setSelection(editNoteEditTitle.length())
                    editNoteEditDescription.setSelection(editNoteEditDescription.length())
                    editNoteEditTitle.requestFocus()
                    val manager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.showSoftInput(editNoteEditTitle, 0)
                }
            } else if (it == false) {
                binding.apply {
                    Toast.makeText(context, "Note edited successfully", Toast.LENGTH_SHORT).show()
                    editNoteTitle.visibility = View.VISIBLE
                    editNoteEditTitle.visibility = View.GONE
                    editNoteDescription.visibility = View.VISIBLE
                    editNoteEditDescription.visibility = View.GONE
                    editNoteViewModel.doneEditingNote(editNoteEditTitle.text.toString(), editNoteEditDescription.text.toString())
                }
            }
        })

        editNoteViewModel.deleteButtonClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                editNoteViewModel.doneDeletingNote()
            }
        })

        return binding.root
    }
}