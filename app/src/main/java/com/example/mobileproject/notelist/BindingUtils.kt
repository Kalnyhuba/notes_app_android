package com.example.mobileproject.notelist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mobileproject.convertCreationDateToString
import com.example.mobileproject.database.Note

@BindingAdapter("noteCreationDateFormatted")
fun TextView.setNoteCreationDateFormatted(item: Note?) {
    item?.let {
        text = convertCreationDateToString(item.timeCreated)
    }
}