package com.hk.ijournal.repository.models;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.time.LocalDate;

public class AccessModel {
    //login livedata
    private MutableLiveData<String> loginUsernameLive;
    private MutableLiveData<String> loginPasscodeLive;
    private MutableLiveData<AccessValidation> loginUserValidation;
    private MutableLiveData<AccessValidation> loginPasscodeValidation;
    //register livedata
    private MutableLiveData<String> registerUsernameLive;
    private MutableLiveData<String> registerPasscodeLive;
    private MutableLiveData<LocalDate> dobLiveData;
    private MutableLiveData<AccessValidation> registerUserValidation;
    private MutableLiveData<AccessValidation> registerPasscodeValidation;

    public enum AccessStatus {
        LOGIN_SUCCESSFUL,
        USER_NOT_FOUND,
        INVALID_LOGIN,
        REGISTER_SUCCESSFULL,
        USER_ALREADY_EXISTS
    }

    public enum AccessValidation {
        USERNAME_INVALID,
        PASSCODE_INVALID,
        DOB_INVALID
    }

    DiaryUser diaryUser;

    public AccessModel() {
        diaryUser = new DiaryUser();
    }

    public MutableLiveData<String> getLoginUsernameLive() {
        if (loginUsernameLive == null)
            loginUsernameLive = new MutableLiveData<>();
        return loginUsernameLive;
    }

    public MutableLiveData<String> getLoginPasscodeLive() {
        if (loginPasscodeLive == null)
            loginPasscodeLive = new MutableLiveData<>();
        return loginPasscodeLive;
    }

    public MutableLiveData<String> getRegisterUsernameLive() {
        if (registerUsernameLive == null)
            registerUsernameLive = new MutableLiveData<>();
        return registerUsernameLive;
    }

    public MutableLiveData<String> getRegisterPasscodeLive() {
        if (registerPasscodeLive == null)
            registerPasscodeLive = new MutableLiveData<>();
        return registerPasscodeLive;
    }

    public MutableLiveData<LocalDate> getDobLiveData() {
        if (dobLiveData == null)
            dobLiveData = new MutableLiveData<>();
        return dobLiveData;
    }

    public DiaryUser getDiaryUser() {
        return diaryUser;
    }

    public void setDiaryUser(DiaryUser diaryUser) {
        this.diaryUser = diaryUser;
    }

    public MutableLiveData<AccessValidation> getLoginUserValidation() {
        if (loginUserValidation == null) {
            //transforming based on username live data
            loginUserValidation = (MutableLiveData<AccessValidation>) Transformations.map(loginUsernameLive, username -> {
                if (isUsernameInvalid(username))
                    return AccessValidation.USERNAME_INVALID;
                return null;
            });
            //setting default value
            loginUserValidation.setValue(AccessValidation.USERNAME_INVALID);
        }
        return loginUserValidation;
    }

    public MutableLiveData<AccessValidation> getLoginPasscodeValidation() {
        if (loginPasscodeValidation == null) {
            loginPasscodeValidation = (MutableLiveData<AccessValidation>) Transformations.map(loginPasscodeLive, passcode -> {
                if (isPassCodeInvalid(passcode))
                    return AccessValidation.PASSCODE_INVALID;
                return null;
            });
            //setting default value
            loginPasscodeValidation.setValue(AccessValidation.PASSCODE_INVALID);
        }
        return loginPasscodeValidation;
    }

    public MutableLiveData<AccessValidation> getRegisterUserValidation() {
        if (registerUserValidation == null) {
            registerUserValidation = (MutableLiveData<AccessValidation>) Transformations.map(registerUsernameLive, username -> {
                if (isUsernameInvalid(username))
                    return AccessValidation.USERNAME_INVALID;
                return null;
            });
            //setting default value
            registerUserValidation.setValue(AccessValidation.USERNAME_INVALID);
        }

        return registerUserValidation;
    }

    public MutableLiveData<AccessValidation> getRegisterPasscodeValidation() {
        if (registerPasscodeValidation == null) {
            registerPasscodeValidation = (MutableLiveData<AccessValidation>) Transformations.map(registerPasscodeLive, passcode -> {
                if (isPassCodeInvalid(passcode))
                    return AccessValidation.PASSCODE_INVALID;
                return null;
            });
            //setting default value
            registerPasscodeValidation.setValue(AccessValidation.PASSCODE_INVALID);
        }
        return registerPasscodeValidation;
    }

    public boolean isUsernameInvalid(String username) {
        return TextUtils.isEmpty(username);
    }

    public boolean isPassCodeInvalid(String passcode) {
        return passcode.length() < 4;
    }

    public boolean isUserValid(MutableLiveData<AccessValidation> userValidation, MutableLiveData<AccessValidation> passcodeValidation) {
        return (userValidation.getValue() == null &&
                passcodeValidation.getValue() == null);
    }

    public AccessStatus processLoginAndGetAccessStatus(DiaryUser diaryUser) {
        return signInWithUser(diaryUser);
    }

    public AccessStatus signInWithUser(DiaryUser dbUser) {
        if (dbUser == null)
            return AccessStatus.USER_NOT_FOUND;
        else if (dbUser.getPasscode().equals(diaryUser.getPasscode()))
            return AccessStatus.LOGIN_SUCCESSFUL;
        else
            return AccessStatus.INVALID_LOGIN;
    }

    public AccessStatus processRegisterAndGetAccessStatus(DiaryUser dbUser) {
        if (dbUser == null) {
            return AccessStatus.REGISTER_SUCCESSFULL;
        } else
            return AccessStatus.USER_ALREADY_EXISTS;
    }
}
