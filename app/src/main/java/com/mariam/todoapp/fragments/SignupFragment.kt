package com.mariam.todoapp.fragments

import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mariam.todoapp.TodoViewModel
import com.mariam.todoapp.dao.User
import com.mariam.todoapp.databinding.FragmentSignupBinding
import com.mariam.todoapp.model.TextValidator

class SignupFragment : Fragment() {
    private lateinit var signupBinding: FragmentSignupBinding
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signupBinding = FragmentSignupBinding.inflate(inflater, container, false)
        return signupBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signupBinding.usernameSignupEt.addTextChangedListener(TextValidator(requireContext(), signupBinding.usernameSignupEt, InputType.TYPE_TEXT_VARIATION_PERSON_NAME))
        signupBinding.emailSignupEt.addTextChangedListener(TextValidator(requireContext(), signupBinding.emailSignupEt, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS))
        signupBinding.passSignupEt.addTextChangedListener(TextValidator(requireContext(), signupBinding.passSignupEt, InputType.TYPE_TEXT_VARIATION_PASSWORD))

        signupBinding.signupBtn.setOnClickListener {
            val username = signupBinding.usernameSignupEt.text.toString().trim()
            val email = signupBinding.emailSignupEt.text.toString().trim()
            val password = signupBinding.passSignupEt.text.toString().trim()
            if(TextValidator.validateData(username, email, password)) {
                viewModel.userExists(email).observe(viewLifecycleOwner) { exists ->
                    if (exists) {
                        Toast.makeText(context, "User already exists.", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.insertUser(User(email = email, username = username, password = password)) { isAdded ->
                            if(isAdded) {
                                Toast.makeText(context, "User added successfully.", Toast.LENGTH_SHORT).show()
                                viewModel.getUserByEmail(email).observe(viewLifecycleOwner) { user ->
                                    Log.d("UserData", "$user")
                                    val action = SignupFragmentDirections.actionSignupFragmentToTodoFragment(user)
                                    findNavController().navigate(action)
                                }
                            }
                            else {
                                Toast.makeText(context, "Failed to add user.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            }
            else {
                Toast.makeText(context,  "Invalid Data.", Toast.LENGTH_SHORT).show()
            }
        }

        setTriggerPassVisibility()
    }

    private fun setTriggerPassVisibility() {
        signupBinding.showPassChb.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                signupBinding.passSignupEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else {
                signupBinding.passSignupEt.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            signupBinding.passSignupEt.setSelection(signupBinding.passSignupEt.text.length)
        }
    }

    override fun onResume() {
        super.onResume()
        signupBinding.usernameSignupEt.apply {
            setText("")
            error = null
        }
        signupBinding.emailSignupEt.apply {
            setText("")
            error = null
        }
        signupBinding.passSignupEt.apply {
            transformationMethod = PasswordTransformationMethod.getInstance()
            setText("")
            error = null
        }
        signupBinding.showPassChb.setOnCheckedChangeListener(null)
        signupBinding.showPassChb.isChecked = false
        setTriggerPassVisibility()
    }
}