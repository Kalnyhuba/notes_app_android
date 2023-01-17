package com.example.mobileproject.notelist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileproject.R
import com.example.mobileproject.database.Note
import com.example.mobileproject.databinding.RecyclerViewNoteListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class NoteAdapter(val onClickEditNoteListener: NoteListener, val onEditNoteClickedListener: NoteListener):
ListAdapter<DataItem, RecyclerView.ViewHolder>(NoteDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    class ConstraintLayoutHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {

            fun from(parent: ViewGroup): ConstraintLayoutHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return ConstraintLayoutHolder(view)
            }
        }
    }

    class ViewHolder private constructor(val binding: RecyclerViewNoteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Note,
            onClickEditNoteListener: NoteListener,
            onEditNoteClickedListener: NoteListener
        ) {
            binding.note = item
            binding.onClickEditNoteListener = onClickEditNoteListener
            binding.onEditNoteClickedListener = onEditNoteClickedListener
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerViewNoteListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> ConstraintLayoutHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItem.NoteItem
                holder.bind(item.note, onClickEditNoteListener, onEditNoteClickedListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.NoteItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<Note>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.NoteItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
}
    class NoteDiffCallback: DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    class NoteListener(val clickListener: (id: Long) -> Unit) {
        fun onClick(note: Note) = clickListener(note.id)
    }

    sealed class DataItem() {
        abstract val id: Long

        data class NoteItem(val note: Note): DataItem() {
            override val id = note.id
        }

        object Header: DataItem() {
            override val id = Long.MIN_VALUE
        }
    }