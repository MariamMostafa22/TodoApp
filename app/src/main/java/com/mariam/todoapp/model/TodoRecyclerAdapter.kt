package com.mariam.todoapp.model

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.mariam.todoapp.R
import com.mariam.todoapp.dao.Todo
import com.mariam.todoapp.dao.TodoEntity
import com.mariam.todoapp.dao.TodoType
import com.mariam.todoapp.databinding.DialogEditTodoBinding
import com.mariam.todoapp.helper.Helper

interface OnTodoClickListener {
    fun onTodoEdited(todo: TodoEntity)
    fun onTodoDeleted(todo: TodoEntity)
}

class TodoRecyclerAdapter(
    val context: Context,
    private val listener: OnTodoClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var todos: MutableList<TodoEntity> = mutableListOf()

    companion object {
        const val VIEW_TYPE_TODO = 1
        const val VIEW_TYPE_TODO_GROUP = 2
    }

    fun setTodoList(todos: MutableList<TodoEntity>?) {
        if(todos != null) {
            this.todos = todos
            notifyDataSetChanged()
        }
    }

    fun addTodoItem(todo: TodoEntity) {
        this.todos.add(todo)
        Log.d("GroupTodo", "Item added.")
        notifyItemInserted(todos.size-1)
    }

    inner class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val todoTitleChb: CheckBox = itemView.findViewById(R.id.todo_title_chb)
        private val todoDescTv: TextView = itemView.findViewById(R.id.todo_desc_tv)
        private val todoItem: CardView = itemView.findViewById(R.id.todo_card)
        private val moreButton: ImageButton = itemView.findViewById(R.id.todo_more_btn)

        fun bind(newTodo: TodoEntity) {
            val position = adapterPosition
            val todo = newTodo.simpleTodo!!
            todoItem.setCardBackgroundColor(newTodo.color)
            todoTitleChb.setOnCheckedChangeListener(null)
            todoTitleChb.isChecked = todo.isDone
            todoTitleChb.setOnCheckedChangeListener { _, isChecked ->
                if (position != RecyclerView.NO_POSITION) {
                    val updatedTodo = todos[position]
                    updatedTodo.simpleTodo!!.isDone = isChecked
                    notifyItemChanged(position)
                    listener.onTodoEdited(todos[position])
                }
            }
            todoTitleChb.text = newTodo.title
            todoTitleChb.paint.isStrikeThruText = todo.isDone
            todoDescTv.text = todo.description ?: ""
            todoDescTv.paint.isStrikeThruText = todo.isDone
            if(todo.description.isNullOrEmpty()) {
                todoDescTv.visibility = View.GONE
            }
            setMoreButton(moreButton, position)
        }
    }

    inner class TodoGroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), OnSubTodoClickListener {
        private val groupTitleTv: TextView = itemView.findViewById(R.id.group_title_tv)
        private val todoGroupView: ListView = itemView.findViewById(R.id.group_todo_lv)
        private val todoItem: CardView = itemView.findViewById(R.id.group_todo_card)
        private val moreButton: ImageButton = itemView.findViewById(R.id.group_more_btn)
        fun bind(todoGroup: TodoEntity) {
            todoItem.setCardBackgroundColor(todoGroup.color)
            groupTitleTv.text = todoGroup.title
            todoGroupView.adapter = TodoGroupAdapter(context, todoGroup.groupTodo!!.todos,this)
            setListViewHeightBasedOnChildren(todoGroupView, 700)
            setMoreButton(moreButton, adapterPosition)
        }
        override fun onSubTodoCheckboxClick() {
            listener.onTodoEdited(todos[adapterPosition])
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (todos[position].type == TodoType.SIMPLE_TODO) VIEW_TYPE_TODO else VIEW_TYPE_TODO_GROUP
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_TODO) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
            TodoViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.group_todo, parent, false)
            TodoGroupViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoViewHolder) {
            holder.bind(todos[position])
        } else if (holder is TodoGroupViewHolder) {
            holder.bind(todos[position])
        }
    }

    fun setListViewHeightBasedOnChildren(listView: ListView, maxHeight: Int) {
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(
                View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = if (totalHeight > maxHeight) maxHeight else totalHeight
        listView.layoutParams = params
        listView.requestLayout()
    }

    private fun setMoreButton(moreButton: ImageButton, position: Int) {
        var toggle = true
        moreButton.setOnClickListener {
            val moreMenu = PopupMenu(context, moreButton)
            moreMenu.menuInflater.inflate(R.menu.menu_more, moreMenu.menu)
            moreMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.edit -> {
                        showEditTodoDialog(position)
                        true
                    }
                    R.id.delete -> {
                        showConfirmDeletionDialog(position)
                        true
                    }
                    else -> false
                }
            }
            if(toggle) moreMenu.show()
            else moreMenu.dismiss()
            toggle = !toggle
        }
    }

    private fun showEditTodoDialog(position: Int) {
        val dialogBinding = DialogEditTodoBinding.inflate(LayoutInflater.from(context))
        val todo = todos[position]
        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .setPositiveButton("Ok") { dialog, _ ->
                val title = dialogBinding.todoTitleEt.text.toString().trim()
                todo.title = title
                if(todo.type == TodoType.SIMPLE_TODO) {
                    todo.simpleTodo!!.description = dialogBinding.todoContentEt.text.toString()
                }
                else if(todo.type == TodoType.TODO_GROUP) {
                    val todosContent = dialogBinding.todoContentEt.text.toString()
                        .split("\n")
                        .map { it.removePrefix("-").trim() }
                    todo.groupTodo!!.todos = todosContent.map { todo -> Todo(name = todo, false) }.toMutableList()
                }
                todos[position] = todo
                notifyItemChanged(position)
                listener.onTodoEdited(todo)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val popupView = Helper.setColorChoices(context) { selectedColor ->
            todo.color = selectedColor
            dialogBinding.todoCard.setCardBackgroundColor(todo.color)
        }
        val okBtn = popupView.findViewById<Button>(R.id.ok_btn)
        okBtn.visibility = View.VISIBLE
        val popupWindow = PopupWindow(
            popupView,
            700,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        okBtn.setOnClickListener {
            popupWindow.dismiss()
        }
        dialogBinding.todoCard.setCardBackgroundColor(todo.color)
        dialogBinding.todoTitleEt.setText(todo.title)
        dialogBinding.changeColorBtn.setOnClickListener {
            popupWindow.showAsDropDown(dialogBinding.changeColorBtn, 0, 0)
        }

        positiveButton.isEnabled = false
        if(todo.type == TodoType.SIMPLE_TODO) {
            if(todo.simpleTodo!!.description.isNullOrEmpty()) {
                dialogBinding.todoContentEt.hint = "Description"
            } else {
                dialogBinding.todoContentEt.setText(todo.simpleTodo!!.description)
            }
            dialogBinding.todoTitleEt.addTextChangedListener {
                positiveButton.isEnabled = dialogBinding.todoTitleEt.text.toString().isNotEmpty()
            }
            dialogBinding.todoContentEt.addTextChangedListener {
                positiveButton.isEnabled = dialogBinding.todoTitleEt.text.toString().isNotEmpty()
            }
        }
        else if(todo.type == TodoType.TODO_GROUP) {
            val todos = todo.groupTodo!!.todos.joinToString(separator = "\n"){ "- " + it.name }
            dialogBinding.todoTitleEt.addTextChangedListener {
                positiveButton.isEnabled = dialogBinding.todoTitleEt.text.toString().isNotEmpty() &&
                        dialogBinding.todoContentEt.text.toString().isNotEmpty()
            }
            dialogBinding.todoContentEt.setText(todos)
            dialogBinding.todoContentEt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if(hasFocus && dialogBinding.todoContentEt.text.isNullOrBlank()) {
                    dialogBinding.todoContentEt.append("- ")
                    dialogBinding.todoContentEt.setSelection(dialogBinding.todoContentEt.text.length)
                }
            }
            dialogBinding.todoContentEt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    positiveButton.isEnabled = dialogBinding.todoTitleEt.text.toString().isNotEmpty() &&
                            dialogBinding.todoContentEt.text.toString().isNotEmpty()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val text = s.toString()
                    if (text.endsWith("\n")) {
                        dialogBinding.todoContentEt.append("- ")
                        dialogBinding.todoContentEt.setSelection(dialogBinding.todoContentEt.text.length)
                    }
                }
            })
        }

    }

    private fun showConfirmDeletionDialog(position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Are you sure you want to delete this to-do?")
            .setPositiveButton("Yes") { _, _ ->
                val todo = todos[position]
                todos.removeAt(position)
                notifyItemRemoved(position)
                listener.onTodoDeleted(todo)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}