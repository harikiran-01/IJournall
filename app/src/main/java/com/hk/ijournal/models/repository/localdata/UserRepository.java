package com.hk.ijournal.models.repository.localdata;

import android.app.Application;
import android.os.AsyncTask;

import com.hk.ijournal.models.DiaryUser;
import com.hk.ijournal.models.repository.localdata.dao.UserDao;
import com.hk.ijournal.models.repository.localdata.db.IJDatabase;

public class UserRepository {
    UserDao userDao;
    public UserRepository(Application application){
        IJDatabase ijDatabase = IJDatabase.getInstance(application.getApplicationContext());
        userDao = ijDatabase.userDao();
    }

    public void insertUser(DiaryUser diaryUser){
        new InsertUserAsyncTask(userDao).execute(diaryUser);
    }

    public void getUserByUserName(String username, FindUserAsyncTask.FindUserAsyncResponse findUserAsyncResponse){
        new FindUserAsyncTask(userDao, findUserAsyncResponse).execute(username);
    }

    private static class InsertUserAsyncTask extends AsyncTask<DiaryUser, Void, Void> {
        UserDao userDao;
        public InsertUserAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(DiaryUser... diaryUsers) {
            userDao.insertUser(diaryUsers[0]);
            return null;
        }
    }

    public static class FindUserAsyncTask extends AsyncTask<String, Void, DiaryUser> {
        public interface FindUserAsyncResponse {
            void foundUser(DiaryUser dbUser);
        }
        FindUserAsyncResponse findUserAsyncResponse = null;

        UserDao userDao;
        public FindUserAsyncTask(UserDao userDao, FindUserAsyncResponse findUserAsyncResponse){
            this.userDao = userDao;
            this.findUserAsyncResponse = findUserAsyncResponse;
        }
        @Override
        protected DiaryUser doInBackground(String... username) {
            return userDao.getUserbyName(username[0]);
        }

        @Override
        protected void onPostExecute(DiaryUser diaryUser) {
            findUserAsyncResponse.foundUser(diaryUser);
        }
    }
}
