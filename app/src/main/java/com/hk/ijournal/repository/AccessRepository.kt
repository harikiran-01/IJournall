package com.hk.ijournal.repository

import android.app.Application
import android.os.AsyncTask
import com.hk.ijournal.repository.local.IJDatabase
import com.hk.ijournal.repository.local.IJDatabase.Companion.getDatabase
import com.hk.ijournal.repository.local.UserDao
import com.hk.ijournal.repository.models.AccessModel
import com.hk.ijournal.repository.models.AccessModel.AccessStatus
import com.hk.ijournal.repository.models.DiaryUser
import java.time.LocalDate

class AccessRepository(application: Application) {
    private val ijDatabase: IJDatabase

    val accessModel: AccessModel
    private var accessScreen = ""

    val userDao: UserDao

    init {
        ijDatabase = getDatabase(application.applicationContext)
        userDao = ijDatabase.userDao()
        accessModel = AccessModel()
    }

    fun setDobLiveData(dob: LocalDate) {
        accessModel.dobLiveData.value = dob
    }

    fun loginUserAndSendAccessData(accessDataResponse: AccessDataResponse) {
        accessScreen = "login"
        accessModel.diaryUser = DiaryUser(accessModel.loginUsernameLive.value.toString(), accessModel.loginPasscodeLive.value.toString())
        if (accessModel.isUserValid(accessModel.getLoginUserValidation(), accessModel.getLoginPasscodeValidation()))
            checkUserInDb(accessDataResponse)
    }

    fun registerUserAndSendAccessData(accessDataResponse: AccessDataResponse) {
        accessScreen = "register"
        accessModel.diaryUser = DiaryUser(accessModel.registerUsernameLive.value.toString(), accessModel.registerPasscodeLive.value.toString(), accessModel.dobLiveData.value)
        if (accessModel.isUserValid(accessModel.getRegisterUserValidation(), accessModel.getRegisterPasscodeValidation()))
            checkUserInDb(accessDataResponse)
    }

    private fun checkUserInDb(accessDataResponse: AccessDataResponse) {
        getUserByUserName(accessModel.diaryUser.username, accessDataResponse)
    }

    fun processAccessAndGetAccessStatus(dbUser: DiaryUser?): AccessStatus {
        return if (accessScreen == "register") {
            val status = accessModel.processRegisterAndGetAccessStatus(dbUser)
            if (status == AccessStatus.REGISTER_SUCCESSFULL) insertUser(accessModel.diaryUser)
            status
        } else accessModel.processLoginAndGetAccessStatus(dbUser)
    }

    private fun getUserByUserName(username: String, accessDataResponse: AccessDataResponse) {
        FindUserAsyncTask(userDao, accessDataResponse).execute(username)
    }

    private fun insertUser(diaryUser: DiaryUser?) {
        InsertUserAsyncTask(userDao).execute(diaryUser)
    }

    fun closeDB() {
        ijDatabase.close()
    }

    interface AccessDataResponse {
        fun onAccessDataReceived(dbUser: DiaryUser?)
    }

    private class InsertUserAsyncTask(private val userDao: UserDao) : AsyncTask<DiaryUser, Void?, Void?>() {
        override fun doInBackground(vararg params: DiaryUser): Void? {
            userDao.insertUser(params[0])
            return null
        }
    }

    class FindUserAsyncTask(private val userDao: UserDao, private val accessResponse: AccessDataResponse) : AsyncTask<String, Void, DiaryUser?>() {
        override fun doInBackground(vararg username: String): DiaryUser? {
            println("dbdeb $username[0]")
            return userDao.getUserbyName(username[0])
        }

        override fun onPostExecute(diaryUser: DiaryUser?) {
            accessResponse.onAccessDataReceived(diaryUser)
        }
    }
}