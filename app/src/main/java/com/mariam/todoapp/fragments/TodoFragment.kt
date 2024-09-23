package com.mariam.todoapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mariam.todoapp.TodoViewModel
import com.mariam.todoapp.dao.Todo
import com.mariam.todoapp.dao.SimpleTodo
import com.mariam.todoapp.dao.TodoEntity
import com.mariam.todoapp.dao.TodoGroup
import com.mariam.todoapp.dao.TodoType
import com.mariam.todoapp.dao.User
import com.mariam.todoapp.databinding.DialogAddTodoBinding
import com.mariam.todoapp.databinding.FragmentTodoBinding
import com.mariam.todoapp.helper.Helper
import com.mariam.todoapp.helper.Helper.colors
import com.mariam.todoapp.model.OnTodoClickListener
import com.mariam.todoapp.model.TodoRecyclerAdapter

class TodoFragment : Fragment(), OnTodoClickListener {
    private lateinit var binding: FragmentTodoBinding
    private val viewModel: TodoViewModel by viewModels()
    private val args: TodoFragmentArgs by navArgs()
    private lateinit var user: User
    private val todoRecyclerView: TodoRecyclerAdapter by lazy {
        TodoRecyclerAdapter(requireContext(), this)
    }
    private var addOptionsVisibility = false
        set(value) {
            binding.addSingleTodoBtn.visibility = if (value) View.VISIBLE else View.GONE
            binding.addGroupTodoBtn.visibility = if (value) View.VISIBLE else View.GONE
            binding.addSingleTodoTv.visibility = if (value) View.VISIBLE else View.GONE
            binding.addGroupTodoTv.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOptionsVisibility = false
        user = args.user
        binding.todoRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.todoRv.adapter = todoRecyclerView
        viewModel.getUserTodoList(user.id).observe(viewLifecycleOwner) { todoList ->
            if(todoList != null) todoRecyclerView.setTodoList(todoList as MutableList)
        }
        binding.addTodoBtn.setOnClickListener {
            addOptionsVisibility = !addOptionsVisibility
        }
        binding.addSingleTodoBtn.setOnClickListener {
            addOptionsVisibility = false
            showAddTodoDialog()
        }
        binding.addGroupTodoBtn.setOnClickListener {
            addOptionsVisibility = false
            showAddGroupTodoDialog()
        }
    }

    private fun showAddTodoDialog() {
        val dialogBinding = DialogAddTodoBinding.inflate(layoutInflater)
        var todoColor = colors[0]
        dialogBinding.groupListEt.visibility = View.GONE
        dialogBinding.root.addView(Helper.setColorChoices(requireContext()) { selectedColor ->
            todoColor = selectedColor
        })
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Todo")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { dialog, _ ->
                var title = dialogBinding.todoTitleEt.text.toString()
                var description = dialogBinding.todoDescEt.text.toString()

                if (title.isEmpty()) {
                    title = description
                    description = ""
                }
                val todo = TodoEntity(
                    title = title,
                    color = todoColor,
                    userId = user.id,
                    type = TodoType.SIMPLE_TODO,
                    simpleTodo = SimpleTodo(description = description, isDone = false,),
                )
                //todoRecyclerView.addTodoItem(todo)
                addTodoToRecyclerView(todo)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

        positiveButton.isEnabled = false

        dialogBinding.todoTitleEt.addTextChangedListener {
            positiveButton.isEnabled = dialogBinding.todoTitleEt.text.toString().isNotEmpty() ||
                    dialogBinding.todoDescEt.text.toString().isNotEmpty()
        }

        dialogBinding.todoDescEt.addTextChangedListener {
            positiveButton.isEnabled = dialogBinding.todoTitleEt.text.toString().isNotEmpty() ||
                    dialogBinding.todoDescEt.text.toString().isNotEmpty()
        }
    }

    private fun showAddGroupTodoDialog() {
        val dialogBinding = DialogAddTodoBinding.inflate(layoutInflater)
        var todoColor = colors[0]
        dialogBinding.todoDescEt.visibility = View.GONE
        dialogBinding.root.addView(Helper.setColorChoices(requireContext()) { selectedColor ->
            todoColor = selectedColor
        })
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Todo Group")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { dialog, _ ->
                val title = dialogBinding.todoTitleEt.text.toString()
                val todosContent = dialogBinding.groupListEt.text.toString()
                    .split("\n")
                    .map { it.removePrefix("-").trim() }
                val todos = todosContent.map { todo -> Todo(name = todo, false) }.toMutableList()
                val todo = TodoEntity(
                    title = title,
                    color = todoColor,
                    userId = user.id,
                    type = TodoType.TODO_GROUP,
                    groupTodo = TodoGroup(todos = todos)
                )
                //todoRecyclerView.addTodoItem(todo)
                addTodoToRecyclerView(todo)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

        positiveButton.isEnabled = false

        dialogBinding.todoTitleEt.addTextChangedListener {
            positiveButton.isEnabled = dialogBinding.todoTitleEt.text.toString().isNotEmpty() &&
                    dialogBinding.groupListEt.text.toString().isNotEmpty()
        }

        dialogBinding.groupListEt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus && dialogBinding.groupListEt.text.isNullOrBlank()) {
                dialogBinding.groupListEt.append("- ")
                dialogBinding.groupListEt.setSelection(dialogBinding.groupListEt.text.length)
            }
        }
        dialogBinding.groupListEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                positiveButton.isEnabled = dialogBinding.todoTitleEt.text.toString().isNotEmpty() &&
                        dialogBinding.groupListEt.text.toString().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()

                if (text.endsWith("\n")) {
                    dialogBinding.groupListEt.append("- ")
                    dialogBinding.groupListEt.setSelection(dialogBinding.groupListEt.text.length)
                }
            }
        })
    }

    private fun addTodoToRecyclerView(todo: TodoEntity) {
        viewModel.insertTodo(todo) { isAdded ->
            if (isAdded) {
                viewModel.getUserTodoList(user.id).observe(viewLifecycleOwner) { todoList ->
                    todoRecyclerView.setTodoList(todoList as MutableList)
                }
            } else {
                Toast.makeText(context, "Failed to add todo.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onTodoEdited(todo: TodoEntity) {
        Log.d("TodoEdit", "${todo.id}")
        viewModel.updateTodo(todo)
    }

    override fun onTodoDeleted(todo: TodoEntity) {
        Log.d("TodoDelete", "${todo.id}")
        viewModel.deleteTodo(todo)
    }

}