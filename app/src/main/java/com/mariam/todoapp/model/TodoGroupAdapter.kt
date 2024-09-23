package com.mariam.todoapp.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import com.mariam.todoapp.R
import com.mariam.todoapp.dao.Todo

interface OnSubTodoClickListener {
    fun onSubTodoCheckboxClick()
}

class TodoGroupAdapter(
    context: Context,
    todos: MutableList<Todo>,
    private val listener: OnSubTodoClickListener
): ArrayAdapter<Todo>(context, 0, todos) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.group_item_todo, parent, false)
        }

        val todo = getItem(position)
        val todoCheckBox: CheckBox = view!!.findViewById(R.id.group_todo_chb)

        todo?.let {
            todoCheckBox.text = it.name
            todoCheckBox.isChecked = it.isDone
            todoCheckBox.paint.isStrikeThruText = it.isDone
            todoCheckBox.setOnCheckedChangeListener { _, isChecked ->
                it.isDone = isChecked
                notifyDataSetChanged()
                listener.onSubTodoCheckboxClick()
            }
        }
        return view
    }
}