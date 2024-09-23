package com.mariam.todoapp.fragments

import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mariam.todoapp.TodoViewModel
import com.mariam.todoapp.databinding.FragmentLoginBinding
import com.mariam.todoapp.model.TextValidator

class LoginFragment : Fragment() {
    private lateinit var loginBinding: FragmentLoginBinding
    private val viewModel: TodoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBinding.emailLoginEt.addTextChangedListener(TextValidator(requireContext(), loginBinding.emailLoginEt, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS))
        loginBinding.passLoginEt.addTextChangedListener(TextValidator(requireContext(), loginBinding.passLoginEt, InputType.TYPE_TEXT_VARIATION_PASSWORD))

        loginBinding.loginBtn.setOnClickListener {
            val email = loginBinding.emailLoginEt.text.toString().trim()
            val password = loginBinding.passLoginEt.text.toString().trim()
            if(TextValidator.validateData("username", email, password)) {
                viewModel.getUserByEmail(email).observe(viewLifecycleOwner) { user ->
                    if (user != null && password == user.password) {
                        val action = LoginFragmentDirections.actionLoginFragmentToTodoFragment(user)
                        findNavController().navigate(action)
                    } else Toast.makeText(context, "Wrong email or password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else {
                Toast.makeText(context, "Invalid data", Toast.LENGTH_SHORT).show()
            }
        }

        setTriggerPassVisibility()
    }
    private fun setTriggerPassVisibility() {
        loginBinding.showPassChb.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                loginBinding.passLoginEt.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else {
                loginBinding.passLoginEt.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            loginBinding.passLoginEt.setSelection(loginBinding.passLoginEt.text.length)
        }
    }

    override fun onResume() {
        super.onResume()
        loginBinding.emailLoginEt.apply {
            setText("")
            error = null
        }
        loginBinding.passLoginEt.apply {
            transformationMethod = PasswordTransformationMethod.getInstance()
            setText("")
            error = null
        }
        loginBinding.showPassChb.setOnCheckedChangeListener(null)
        loginBinding.showPassChb.isChecked = false
        setTriggerPassVisibility()
    }
}