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

import com.hk.ijournal.R;
import com.hk.ijournal.adapters.AccessBindingAdapter;
import com.hk.ijournal.databinding.FragmentRegisterBinding;
import com.hk.ijournal.viewmodels.AccessViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

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

        accessViewModel = LaunchActivity.obtainViewModel(requireActivity());
        registerBinding.setLifecycleOwner(getActivity());
        registerBinding.setAccessViewModel(accessViewModel);
        registerBinding.setAccessBindingAdapter(new AccessBindingAdapter());
        return registerBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePickerDialog = new DatePickerDialog(requireContext(), this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.setOnDateSetListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeViewModel(accessViewModel);
    }

    private void observeViewModel(AccessViewModel accessViewModel) {

        accessViewModel.getAccessStatus().observe(this.getViewLifecycleOwner(), accessStatus -> {
            switch (accessStatus) {
                case REGISTER_SUCCESSFULL: {
                    Toasty.info(requireActivity(), "Register Successful!", Toasty.LENGTH_SHORT, true).show();
                    break;
                }
                case USER_ALREADY_EXISTS: {
                    Toasty.info(requireActivity(), "User already exists!", Toasty.LENGTH_SHORT, true).show();
                    break;
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
