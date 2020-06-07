package com.hk.ijournal.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hk.ijournal.R;
import com.hk.ijournal.databinding.FragmentLoginBinding;
import com.hk.ijournal.models.AccessModel;
import com.hk.ijournal.viewmodels.AccessViewModel;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private AccessViewModel accessViewModel;
    private FragmentLoginBinding loginBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Login frag created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        loginBinding.loginbutton.setOnClickListener(this);
        return loginBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accessViewModel = ViewModelProviders.of(this).get(AccessViewModel.class);
        loginBinding.setLifecycleOwner(this);
        loginBinding.setAccessViewModel(accessViewModel);
        observeViewModel(accessViewModel);
    }

    private void observeViewModel(AccessViewModel accessViewModel) {

        accessViewModel.getUser().observe(this, new Observer<AccessModel>() {
            @Override
            public void onChanged(AccessModel accessModel) {

            }
        });

        accessViewModel.getAccessStatus().observe(this, new Observer<AccessModel.AccessStatus>() {
            @Override
            public void onChanged(AccessModel.AccessStatus accessStatus) {
                switch (accessStatus){
                    case LOGIN_SUCCESSFUL:{
                        Toasty.info(Objects.requireNonNull(getActivity()), "Login Successful!", Toasty.LENGTH_SHORT, true).show();
                        launchHomeActivity();
                        break;
                    }
                    case INVALID_LOGIN:{
                        Toasty.info(Objects.requireNonNull(getActivity()), "Invalid Password!", Toasty.LENGTH_SHORT, true).show();
                        break;
                    }
                    case USER_NOT_FOUND:{
                        Toasty.info(Objects.requireNonNull(getActivity()), "User doesn't exist!", Toasty.LENGTH_SHORT, true).show();
                        break;
                    }
                }
            }
        });
    }

    private void launchHomeActivity() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginbutton:{
                accessViewModel.loginUser();
                break;
            }
        }
    }
}
