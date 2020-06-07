package com.hk.ijournal.models;

import androidx.lifecycle.MutableLiveData;

import com.hk.ijournal.models.repository.localdata.UserRepository;

public abstract class AccessModel implements UserRepository.FindUserAsyncTask.FindUserAsyncResponse{
    public interface AccessData {
        void onAccessDataReceived(DiaryUser dbUser);
    }

    public enum AccessStatus{
        LOGIN_SUCCESSFUL,
        USER_NOT_FOUND,
        INVALID_LOGIN,
        REGISTER_SUCCESSFULL,
        USER_ALREADY_EXISTS
    }

    public enum AccessValidation{
        USERNAME_INVALID,
        PASSCODE_INVALID,
        DOB_INVALID
    }

    AccessData accessData;
    MutableLiveData<AccessValidation> accessValidation;
    DiaryUser diaryUser;

    public AccessModel() {
    }

    public String getUsername() {
        return diaryUser.getUsername();
    }

    public String getPasscode() {
        return diaryUser.getPasscode();
    }


    public boolean isUserNameInvalid(){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        //|| !user.getUsername().matches(regex)
        return (diaryUser.getUsername().isEmpty() );
    }

    public boolean isPassCodeInvalid(){
        return (diaryUser.getPasscode().isEmpty() || diaryUser.getPasscode().length()<4);
    }

    public abstract void checkUserinDb(UserRepository userRepository, AccessData accessData);

    public abstract AccessStatus processAccess(DiaryUser diaryUser);
}
