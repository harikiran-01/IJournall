package com.hk.ijournal.dayentry.adapters.binders

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.google.android.material.textfield.TextInputEditText

object TextContentBindingAdapter {

    @BindingAdapter("textContent")
    @JvmStatic fun setTextContent(editText: TextInputEditText, newValue: String) {
        // Important to break potential infinite loops.
        if (editText.text.toString() != newValue) {
            editText.setText(newValue)
        }
    }

    @InverseBindingAdapter(attribute = "textContent")
    @JvmStatic fun getTextContent(editText: TextInputEditText) : String {
        return editText.text.toString()
    }
}