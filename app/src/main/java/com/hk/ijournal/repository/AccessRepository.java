package com.hk.ijournal.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.hk.ijournal.repository.local.IJDatabase;
import com.hk.ijournal.repository.local.UserDao;
import com.hk.ijournal.repository.models.AccessModel;
import com.hk.ijournal.repository.models.DiaryUser;

import java.time.LocalDate;

public class AccessRepository {
    AccessDataResponse accessDataResponse;
    AccessModel accessModel;
    private String accessScreen = "";

    public AccessRepository(Application application) {
        IJDatabase ijDatabase = IJDatabase.getInstance(application.getApplicationContext());
        userDao = ijDatabase.userDao();
        accessModel = new AccessModel();
        accessDataResponse = null;
    }

    UserDao userDao;

    public MutableLiveData<String> getLoginUsernameLive() {
        return accessModel.getLoginUsernameLive();
    }

    public MutableLiveData<String> getLoginPasscodeLive() {
        return accessModel.getLoginPasscodeLive();
    }

    public MutableLiveData<String> getRegisterUsernameLive() {
        return accessModel.getRegisterUsernameLive();
    }

    public MutableLiveData<String> getRegisterPasscodeLive() {
        return accessModel.getRegisterPasscodeLive();
    }

    public MutableLiveData<LocalDate> getDobLiveData() {
        return accessModel.getDobLiveData();
    }

    public void setDobLiveData(LocalDate dob) {
        accessModel.getDobLiveData().setValue(dob);
    }

    public void loginUserAndSendAccessData(AccessDataResponse accessDataResponse) {
        accessScreen = "login";
        accessModel.setDiaryUser(new DiaryUser(accessModel.getLoginUsernameLive().getValue(), accessModel.getLoginPasscodeLive().getValue()));
        if (accessModel.isUserValid(accessModel.getLoginUserValidation(), accessModel.getLoginPasscodeValidation()))
            checkUserInDb(accessDataResponse);
    }

    public void registerUserAndSendAccessData(AccessDataResponse accessDataResponse) {
        accessScreen = "register";
        accessModel.setDiaryUser(new DiaryUser(accessModel.getRegisterUsernameLive().getValue(), accessModel.getRegisterPasscodeLive().getValue(), accessModel.getDobLiveData().getValue()));
        if (accessModel.isUserValid(accessModel.getRegisterUserValidation(), accessModel.getRegisterPasscodeValidation()))
            checkUserInDb(accessDataResponse);
    }

    private void checkUserInDb(AccessDataResponse accessDataResponse) {
        getUserByUserName(accessModel.getDiaryUser().getUsername(), accessDataResponse);
    }

    public MutableLiveData<AccessModel.AccessValidation> getLoginUserValidation() {
        return accessModel.getLoginUserValidation();
    }

    public MutableLiveData<AccessModel.AccessValidation> getLoginPasscodeValidation() {
        return accessModel.getLoginPasscodeValidation();
    }

    public MutableLiveData<AccessModel.AccessValidation> getRegisterUserValidation() {
        return accessModel.getRegisterUserValidation();
    }

    public MutableLiveData<AccessModel.AccessValidation> getRegisterPasscodeValidation() {
        return accessModel.getRegisterPasscodeValidation();
    }

    public AccessModel.AccessStatus processAccessAndGetAccessStatus(DiaryUser dbUser) {
        if (accessScreen.equals("register")) {
            AccessModel.AccessStatus status = accessModel.processRegisterAndGetAccessStatus(dbUser);
            if (status.equals(AccessModel.AccessStatus.REGISTER_SUCCESSFULL))
                insertUser(accessModel.getDiaryUser());
            return status;
        } else
            return accessModel.processLoginAndGetAccessStatus(dbUser);

    }

    public void getUserByUserName(String username, AccessDataResponse accessDataResponse) {
        new FindUserAsyncTask(userDao, accessDataResponse).execute(username);
    }

    public void insertUser(DiaryUser diaryUser) {
        new InsertUserAsyncTask(userDao).execute(diaryUser);
    }

    public interface AccessDataResponse {
        void onAccessDataReceived(DiaryUser dbUser);
    }

    private static class InsertUserAsyncTask extends AsyncTask<DiaryUser, Void, Void> {
        UserDao userDao;

        public InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(DiaryUser... diaryUsers) {
            userDao.insertUser(diaryUsers[0]);
            return null;
        }
    }

    public static class FindUserAsyncTask extends AsyncTask<String, Void, DiaryUser> {

        AccessDataResponse accessDataResponse = null;

        UserDao userDao;

        public FindUserAsyncTask(UserDao userDao, AccessDataResponse accessDataResponse) {
            this.userDao = userDao;
            this.accessDataResponse = accessDataResponse;
        }

        @Override
        protected DiaryUser doInBackground(String... username) {
            return userDao.getUserbyName(username[0]);
        }

        @Override
        protected void onPostExecute(DiaryUser diaryUser) {
            accessDataResponse.onAccessDataReceived(diaryUser);
        }
    }
}
