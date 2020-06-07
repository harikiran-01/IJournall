package com.hk.ijournal.models;

import com.hk.ijournal.models.repository.localdata.UserRepository;

import java.time.LocalDate;

public class RegisterModel extends AccessModel {
    UserRepository userRepository;

    public RegisterModel(String username, String passcode, LocalDate dob) {
        diaryUser = new DiaryUser(username,passcode, dob);
    }

    @Override
    public void foundUser(DiaryUser dbUser) {
        accessData.onAccessDataReceived(dbUser);
    }

    @Override
    public void checkUserinDb(UserRepository userRepository, AccessData accessData) {
        this.userRepository = userRepository;
        this.accessData = accessData;
        userRepository.getUserByUserName(diaryUser.getUsername(), this);
    }

    @Override
    public AccessStatus processAccess(DiaryUser dbUser) {
        if(dbUser==null){
            userRepository.insertUser(diaryUser);
            return AccessStatus.REGISTER_SUCCESSFULL;}
        else
            return AccessStatus.USER_ALREADY_EXISTS;
    }
}
