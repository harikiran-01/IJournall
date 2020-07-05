package com.hk.ijournal.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.hk.ijournal.R;
import com.hk.ijournal.adapters.AccessBindingAdapter;
import com.hk.ijournal.databinding.FragmentLoginBinding;
import com.hk.ijournal.viewmodels.AccessViewModel;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private AccessViewModel accessViewModel;
    private FragmentLoginBinding loginBinding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lifecycle", "loginF onCreateView");
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        loginBinding.loginbutton.setOnClickListener(this);
        loginBinding.setLifecycleOwner(getViewLifecycleOwner());
        loginBinding.setAccessBindingAdapter(AccessBindingAdapter.INSTANCE);
        return loginBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("lifecycle", "loginF onActivityCreated");
        accessViewModel = LaunchActivity.obtainViewModel(requireActivity());
        loginBinding.setAccessViewModel(accessViewModel);
        observeViewModel(accessViewModel);
    }

    private void observeViewModel(AccessViewModel accessViewModel) {

        accessViewModel.getAccessStatus().observe(getViewLifecycleOwner(), accessStatus -> {
            Log.d("lifecycle", "loginF observeViewModel");
            switch (accessStatus) {
                case LOGIN_SUCCESSFUL: {
                    Toasty.info(requireActivity(), "Login Successful!", Toasty.LENGTH_SHORT, true).show();
                    LoginFragment.this.launchHomeActivity();
                    break;
                }
                case INVALID_LOGIN: {
                    Toasty.info(requireActivity(), "Invalid Password!", Toasty.LENGTH_SHORT, true).show();
                    break;
                }
                case USER_NOT_FOUND: {
                    Toasty.info(requireActivity(), "User doesn't exist!", Toasty.LENGTH_SHORT, true).show();
                    break;
                }
            }
        });
    }

    private void launchHomeActivity() {
        ((LaunchActivity) getActivity()).navigateTo(new HomeFragment(), false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginbutton) {
            accessViewModel.loginUser();
        }
    }
}
