package com.hk.ijournal.adapters;

import android.text.TextUtils;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hk.ijournal.R;
import com.hk.ijournal.repository.models.AccessModel;

import java.time.LocalDate;

public class AccessBindingAdapter {
    public MutableLiveData<Boolean> loginusernameFocusLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loginpasscodeFocusLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> registerusernameFocusLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> registerpasscodeFocusLiveData = new MutableLiveData<>();

    public AccessBindingAdapter() {
        loginusernameFocusLiveData.setValue(true);
        loginpasscodeFocusLiveData.setValue(true);
        registerusernameFocusLiveData.setValue(true);
        registerpasscodeFocusLiveData.setValue(true);
    }


    @BindingAdapter(value = {"focusState", "errorMsg"}, requireAll = false)
    public static void setErrorMsg(TextInputLayout textInputLayout, boolean focusState, String errorMessage) {
        if (!focusState) {
            if (TextUtils.isEmpty(errorMessage))
                textInputLayout.setError(null);
            else
                textInputLayout.setError(errorMessage);
        } else
            textInputLayout.setError(null);
    }

    @BindingAdapter(value = {"enableOnUserValid", "enableOnPassValid", "screenData", "enableOnDateValid"}, requireAll = false)
    public static void setToggleEnable(Button button, AccessModel.AccessValidation userValidation, AccessModel.AccessValidation passcodeValidation, String screenData, LocalDate dob) {
        boolean isFormInvalid = userValidation != null || passcodeValidation != null;
        if (!TextUtils.isEmpty(screenData)) {
            if (screenData.equals("register")) {
                isFormInvalid = isFormInvalid || dob == null;
            }
        }
        button.setEnabled(!isFormInvalid);
    }

    @BindingAdapter("onFocusChange")
    public static void onFocusChange(TextInputEditText editText, OnFocusChangeListener onFocusChangeListener) {
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }

    public OnFocusChangeListener getFocusChangeListener() {
        return (v, hasFocus) -> {
            switch (v.getId()) {
                case R.id.loginusername: {
                    loginusernameFocusLiveData.setValue(hasFocus);
                    break;
                }
                case R.id.loginpasscode: {
                    loginpasscodeFocusLiveData.setValue(hasFocus);
                    break;
                }
                case R.id.registerusername: {
                    registerusernameFocusLiveData.setValue(hasFocus);
                    break;
                }
                case R.id.registerpasscode: {
                    registerpasscodeFocusLiveData.setValue(hasFocus);
                    break;
                }
            }

        };
    }
}
