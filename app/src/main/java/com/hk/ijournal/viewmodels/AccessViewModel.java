package com.hk.ijournal.viewmodels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hk.ijournal.models.AccessModel;
import com.hk.ijournal.models.DiaryUser;
import com.hk.ijournal.models.LoginModel;
import com.hk.ijournal.models.RegisterModel;
import com.hk.ijournal.models.repository.localdata.UserRepository;

import java.time.LocalDate;

public class AccessViewModel extends AndroidViewModel implements LoginModel.AccessData {
    AccessModel accessModel;
    UserRepository userRepository;
    public MutableLiveData<String> Username = new MutableLiveData<>();
    public MutableLiveData<String> Passcode = new MutableLiveData<>();
    public MutableLiveData<LocalDate> Dob = new MutableLiveData<>();
    public MutableLiveData<AccessModel.AccessStatus> accessStatus;
    private MutableLiveData<AccessModel> userMutableLiveData;

    public AccessViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public MutableLiveData<AccessModel> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public MutableLiveData<AccessModel.AccessStatus> getAccessStatus(){
        if(accessStatus == null)
            accessStatus = new MutableLiveData<>();
        return accessStatus;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onDateSelected(int year, int month, int dayOfMonth){
        Dob.setValue(LocalDate.of(year, month, dayOfMonth));
    }

    public void loginUser() {
        System.out.println("clickede "+Username.getValue()+" "+Passcode.getValue());
        accessModel = new LoginModel(Username.getValue(), Passcode.getValue());
        userMutableLiveData.setValue(accessModel);
        accessModel.checkUserinDb(userRepository, this);
    }

    public void registerUser(){
        accessModel = new RegisterModel(Username.getValue(), Passcode.getValue(), Dob.getValue());
        userMutableLiveData.setValue(accessModel);
        accessModel.checkUserinDb(userRepository, this);
    }

    @Override
    public void onAccessDataReceived(DiaryUser dbUser) {
        accessStatus.postValue(accessModel.processAccess(dbUser));
    }
}
