package com.hk.ijournal.views;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hk.ijournal.R;
import com.hk.ijournal.databinding.FragmentRegisterBinding;
import com.hk.ijournal.models.AccessModel;
import com.hk.ijournal.viewmodels.AccessViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class RegisterFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    DatePickerDialog datePickerDialog;
    AccessViewModel accessViewModel;
    FragmentRegisterBinding registerBinding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        registerBinding.dateselector.setOnClickListener(this);
        registerBinding.registerbutton.setOnClickListener(this);
        return registerBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH)+1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.setOnDateSetListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accessViewModel = ViewModelProviders.of(this).get(AccessViewModel.class);
        registerBinding.setLifecycleOwner(this);
        registerBinding.setAccessViewModel(accessViewModel);
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
                    case REGISTER_SUCCESSFULL:{
                        Toasty.info(Objects.requireNonNull(getActivity()), "Register Successful!", Toasty.LENGTH_SHORT, true).show();
                        break;
                    }
                    case USER_ALREADY_EXISTS:{
                        Toasty.info(Objects.requireNonNull(getActivity()), "User already exists!", Toasty.LENGTH_SHORT, true).show();
                        break;
                    }
                }
            }
        });
    }

    public void showDatePicker() {
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        accessViewModel.onDateSelected(year, month, dayOfMonth);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dateselector:{
                showDatePicker();
                break;
            }
            case R.id.registerbutton:{
                accessViewModel.registerUser();
                break;
            }
        }
    }
}
