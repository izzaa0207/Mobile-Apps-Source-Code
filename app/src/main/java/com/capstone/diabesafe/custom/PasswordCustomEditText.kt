package com.capstone.diabesafe.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.capstone.diabesafe.R

class PasswordCustomEditText (context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val passwordPattern = "^.{8,20}$"
                if (!s.isNullOrEmpty() && !s.matches(passwordPattern.toRegex())) {
                    error = context.getString(R.string.password_invalid)
                }
            }
        })
    }
}