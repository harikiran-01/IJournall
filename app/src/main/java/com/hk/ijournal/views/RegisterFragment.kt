package com.hk.ijournal.views

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hk.ijournal.R
import com.hk.ijournal.adapters.AccessBindingAdapter
import com.hk.ijournal.databinding.FragmentRegisterBinding
import com.hk.ijournal.repository.models.AccessModel.AccessStatus
import com.hk.ijournal.viewmodels.AccessViewModel
import com.hk.ijournal.views.LaunchActivity.Companion.obtainViewModel
import es.dmoral.toasty.Toasty
import java.util.*

class RegisterFragment : Fragment(), View.OnClickListener, OnDateSetListener {
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var accessViewModel: AccessViewModel
    private lateinit var registerBinding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        registerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        registerBinding.dateselector.setOnClickListener(this)
        registerBinding.registerbutton.setOnClickListener(this)
        registerBinding.lifecycleOwner = viewLifecycleOwner
        registerBinding.accessBindingAdapter = AccessBindingAdapter
        return registerBinding.root
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePickerDialog = DatePickerDialog(requireContext(), this, Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH] + 1,
                Calendar.getInstance()[Calendar.DAY_OF_MONTH])
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.setOnDateSetListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        accessViewModel = obtainViewModel(requireActivity())
        registerBinding.accessViewModel = accessViewModel
        observeViewModel(accessViewModel)
    }

    private fun observeViewModel(accessViewModel: AccessViewModel) {
        accessViewModel.accessStatus.observe(this.viewLifecycleOwner, Observer { accessStatus: AccessStatus? ->
            when (accessStatus) {
                AccessStatus.REGISTER_SUCCESSFULL -> {
                    Toasty.info(requireActivity(), "Register Successful!", Toasty.LENGTH_SHORT, true).show()
                    onRegisterSuccessful()
                }
                AccessStatus.USER_ALREADY_EXISTS -> {
                    Toasty.info(requireActivity(), "User already exists!", Toasty.LENGTH_SHORT, true).show()
                }
            }
        })
    }

    private fun onRegisterSuccessful() {
        (requireActivity() as LaunchActivity).launchHomeFragment()
    }

    fun showDatePicker() {
        datePickerDialog.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        accessViewModel.onDateSelected(year, month, dayOfMonth)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dateselector -> {
                showDatePicker()
            }
            R.id.registerbutton -> {
                accessViewModel.registerUser()
            }
        }
    }
}