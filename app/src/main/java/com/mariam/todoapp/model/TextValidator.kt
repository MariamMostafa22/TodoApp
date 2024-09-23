package com.mariam.todoapp.model

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import com.mariam.todoapp.R

class TextValidator(val context: Context, private val editText: EditText, private val type: Int): TextWatcher{

    companion object{
        fun validateData(username: String, email: String, password: String): Boolean {
            return validateUsername(username) && validateEmail(email) && validatePassword(password)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
        val text = editText.text.toString().trim()
        if(type == InputType.TYPE_TEXT_VARIATION_PERSON_NAME) {
            if (!validateUsername(text)) {
                editText.error = "Username must be between 3 and 15 characters long"
            } else {
                showCorrect()
            }
        }
        else if(type == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
            if (!validateEmail(text)) {
                editText.error = "Invalid email address"
            } else {
                showCorrect()
            }

        }
        else if(type == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            if (!validatePassword(text)) {
                editText.error = "Password must be at least 8 characters long"
            } else {
                showCorrect()
            }
        }
    }

    private fun showCorrect() {
        editText.error = null
        val correct = AppCompatResources.getDrawable(context,R.drawable.baseline_check_24)
        val h = correct!!.intrinsicHeight
        val w = correct.intrinsicWidth
        correct.setBounds( 0, 0, w, h )
        correct.setTintList(null)
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(editText.compoundDrawables[0], null, correct, null)
    }
}

fun validateUsername(username: String): Boolean {
    return username.isNotEmpty() && username.length in 3..15
}

fun validateEmail(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password: String): Boolean {
    return password.length >= 8
}