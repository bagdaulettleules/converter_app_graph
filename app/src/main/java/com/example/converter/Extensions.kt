package com.example.converter

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText


/**
 * Created by Bagdaulet Tleules on 12.02.2022.
 * email: bagdaulettleules@gmail.com
 */

fun AppCompatEditText.afterTextChanged(afterTextChangedAction: (text: String?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            val text = if (p0.isNullOrEmpty()) null else p0.toString()
            afterTextChangedAction.invoke(text)
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}