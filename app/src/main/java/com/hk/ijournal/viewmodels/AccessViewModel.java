package com.hk.ijournal.viewmodels;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hk.ijournal.repository.AccessRepository;
import com.hk.ijournal.repository.models.AccessModel;
import com.hk.ijournal.repository.models.DiaryUser;

import java.time.LocalDate;

public class AccessViewModel extends AndroidViewModel implements AccessRepository.AccessDataResponse {
    //username and passcode are two way binding variables
    //login livedata
    public MutableLiveData<String> loginUsernameLive;
    public MutableLiveData<String> loginPasscodeLive;
    public LiveData<AccessModel.AccessValidation> loginUserValidation;
    public LiveData<AccessModel.AccessValidation> loginPasscodeValidation;
    //register livedata
    public MutableLiveData<String> registerUsernameLive;
    public MutableLiveData<String> registerPasscodeLive;
    public MutableLiveData<LocalDate> dobLiveData;
    public LiveData<AccessModel.AccessValidation> registerUserValidation;
    public LiveData<AccessModel.AccessValidation> registerPasscodeValidation;
    AccessRepository accessRepository;
    public MutableLiveData<AccessModel.AccessStatus> accessStatus;

    public AccessViewModel(@NonNull Application application) {
        super(application);
        Log.d("lifecycle", "accessVM constructor");
        accessRepository = new AccessRepository(application);
        bindLiveData();
    }

    private void bindLiveData() {
        //login binding
        loginUsernameLive = accessRepository.getLoginUsernameLive();
        loginPasscodeLive = accessRepository.getLoginPasscodeLive();
        loginUserValidation = accessRepository.getLoginUserValidation();
        loginPasscodeValidation = accessRepository.getLoginPasscodeValidation();
        //register binding
        registerUsernameLive = accessRepository.getRegisterUsernameLive();
        registerPasscodeLive = accessRepository.getRegisterPasscodeLive();
        registerUserValidation = accessRepository.getRegisterUserValidation();
        registerPasscodeValidation = accessRepository.getRegisterPasscodeValidation();
        dobLiveData = accessRepository.getDobLiveData();
    }

    public LiveData<AccessModel.AccessStatus> getAccessStatus() {
        if (accessStatus == null)
            accessStatus = new MutableLiveData<>();
        return accessStatus;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onDateSelected(int year, int month, int dayOfMonth) {
        accessRepository.setDobLiveData(LocalDate.of(year, month, dayOfMonth));
    }

    public void loginUser() {
        accessRepository.loginUserAndSendAccessData(this);
    }

    public void registerUser() {
        accessRepository.registerUserAndSendAccessData(this);
    }

    @Override
    public void onAccessDataReceived(DiaryUser dbUser) {
        accessStatus.setValue(accessRepository.processAccessAndGetAccessStatus(dbUser));
    }
}
