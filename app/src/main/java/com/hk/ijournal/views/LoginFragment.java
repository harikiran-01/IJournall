package com.hk.ijournal.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.hk.ijournal.R;
import com.hk.ijournal.adapters.AccessBindingAdapter;
import com.hk.ijournal.databinding.FragmentLoginBinding;
import com.hk.ijournal.viewmodels.AccessViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private AccessViewModel accessViewModel;
    private FragmentLoginBinding loginBinding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        loginBinding.loginbutton.setOnClickListener(this);
        return loginBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accessViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AccessViewModel.class);
        loginBinding.setLifecycleOwner(getActivity());
        loginBinding.setAccessViewModel(accessViewModel);
        loginBinding.setAccessBindingAdapter(new AccessBindingAdapter());
        observeViewModel(accessViewModel);
    }

    private void observeViewModel(AccessViewModel accessViewModel) {

        accessViewModel.getAccessStatus().observe(this.getViewLifecycleOwner(), accessStatus -> {
            switch (accessStatus) {
                case LOGIN_SUCCESSFUL: {
                    Toasty.info(Objects.requireNonNull(getActivity()), "Login Successful!", Toasty.LENGTH_SHORT, true).show();
                    LoginFragment.this.launchHomeActivity();
                    break;
                }
                case INVALID_LOGIN: {
                    Toasty.info(Objects.requireNonNull(getActivity()), "Invalid Password!", Toasty.LENGTH_SHORT, true).show();
                    break;
                }
                case USER_NOT_FOUND: {
                    Toasty.info(Objects.requireNonNull(getActivity()), "User doesn't exist!", Toasty.LENGTH_SHORT, true).show();
                    break;
                }
            }
        });
    }

    private void launchHomeActivity() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginbutton) {
            accessViewModel.loginUser();
        }
    }
}
