package com.hk.ijournal.adapters

import android.text.TextUtils
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.hk.ijournal.R
import com.hk.ijournal.repository.AccessRepository.AccessValidation
import java.time.LocalDate

object AccessBindingAdapter {
    val loginusernameFocusLiveData = MutableLiveData<Boolean>()
    val loginpasscodeFocusLiveData = MutableLiveData<Boolean>()
    val registerusernameFocusLiveData = MutableLiveData<Boolean>()
    val registerpasscodeFocusLiveData = MutableLiveData<Boolean>()

    val focusChangeListener: OnFocusChangeListener
        get() = OnFocusChangeListener { v: View, hasFocus: Boolean ->
            when (v.id) {
                R.id.loginusername -> {
                    loginusernameFocusLiveData.setValue(hasFocus)
                }
                R.id.loginpasscode -> {
                    loginpasscodeFocusLiveData.setValue(hasFocus)
                }
                R.id.registerusername -> {
                    registerusernameFocusLiveData.setValue(hasFocus)
                }
                R.id.registerpasscode -> {
                    registerpasscodeFocusLiveData.setValue(hasFocus)
                }
            }
        }

    @JvmStatic
    @BindingAdapter(value = ["focusState", "errorMsg"], requireAll = false)
    fun setErrorMsg(textInputLayout: TextInputLayout, focusState: Boolean, errorMessage: String?) {
        if (!focusState) {
            if (TextUtils.isEmpty(errorMessage)) textInputLayout.error = null else textInputLayout.error = errorMessage
        } else textInputLayout.error = null
    }

    @JvmStatic
    @BindingAdapter(value = ["enableOnUserValid", "enableOnPassValid", "screenData", "enableOnDateValid"], requireAll = false)
    fun setToggleEnable(button: Button, userValidation: AccessValidation?, passcodeValidation: AccessValidation?, screenData: String?, dob: LocalDate?) {
        var isFormInvalid = userValidation != null || passcodeValidation != null
        if (!TextUtils.isEmpty(screenData)) {
            if (screenData == "register") isFormInvalid = isFormInvalid || dob == null
        }
        button.isEnabled = !isFormInvalid
    }

    @JvmStatic
    @BindingAdapter("onFocusChange")
    fun onFocusChange(editText: TextInputEditText, onFocusChangeListener: OnFocusChangeListener?) {
        editText.onFocusChangeListener = onFocusChangeListener
    }

    init {
        loginusernameFocusLiveData.value = true
        loginpasscodeFocusLiveData.value = true
        registerusernameFocusLiveData.value = true
        registerpasscodeFocusLiveData.value = true
    }
}