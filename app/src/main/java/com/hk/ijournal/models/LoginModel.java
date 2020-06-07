package com.hk.ijournal.models;

import com.hk.ijournal.models.repository.localdata.UserRepository;

public class LoginModel extends AccessModel {

    public LoginModel(String username, String passcode) {
        diaryUser = new DiaryUser(username,passcode);
    }

    @Override
    public void foundUser(DiaryUser dbUser) {
        accessData.onAccessDataReceived(dbUser);
    }

    public AccessStatus signInWithUser(DiaryUser dbUser) {
        if(dbUser==null)
            return AccessStatus.USER_NOT_FOUND;
        else if(dbUser.getPasscode().equals(diaryUser.getPasscode()))
            return AccessStatus.LOGIN_SUCCESSFUL;
        else
            return AccessStatus.INVALID_LOGIN;
    }

    @Override
    public void checkUserinDb(UserRepository userRepository, AccessData accessData) {
        this.accessData = accessData;
        userRepository.getUserByUserName(diaryUser.getUsername(), this);
    }

    @Override
    public AccessStatus processAccess(DiaryUser diaryUser) {
        return signInWithUser(diaryUser);
    }
}
