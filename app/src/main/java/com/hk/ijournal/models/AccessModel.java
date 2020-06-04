package com.hk.ijournal.models;

public abstract class AccessModel {
    DiaryUser diaryUser;

    public AccessModel(DiaryUser diaryUser) {
        this.diaryUser = diaryUser;
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

    public boolean userExistsinDB(){
        return false;
        //return ijDatabase.userDAO().getUserbyName(user.getUsername())!=null;
    }
}
