package com.bliss.auth.ui.adapters

import android.text.TextUtils
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bliss.auth.R
import com.bliss.auth.repo.AccessRepositoryImpl
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
    fun setErrorMsg(textInputLayout: TextInputLayout, focusState: Boolean, errorMessage: AccessRepositoryImpl.AccessValidation?) {
        if (!focusState) {
            errorMessage?.let {
                textInputLayout.error = when (it) {
                    AccessRepositoryImpl.AccessValidation.USERNAME_INVALID -> "Invalid Username"
                    AccessRepositoryImpl.AccessValidation.PASSCODE_INVALID -> "Passcode Invalid"
                    else -> null
                }
            }
        } else textInputLayout.error = null
    }

    @JvmStatic
    @BindingAdapter(value = ["enableOnUserValid", "enableOnPassValid", "screenData", "enableOnDateValid"], requireAll = false)
    fun setToggleEnable(button: Button, userValidation: AccessRepositoryImpl.AccessValidation?, passcodeValidation: AccessRepositoryImpl.AccessValidation?, screenData: String?, dob: LocalDate?) {
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