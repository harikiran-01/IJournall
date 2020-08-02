package com.hk.ijournal.views.access

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
import androidx.lifecycle.ViewModelProvider
import com.hk.ijournal.R
import com.hk.ijournal.adapters.AccessBindingAdapter
import com.hk.ijournal.databinding.FragmentRegisterBinding
import com.hk.ijournal.repository.AccessRepository.RegisterStatus
import com.hk.ijournal.viewmodels.AccessViewModel
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
        accessViewModel = ViewModelProvider(requireParentFragment()).get(AccessViewModel::class.java)
        registerBinding.accessViewModel = accessViewModel
        registerBinding.lifecycleOwner = viewLifecycleOwner
        registerBinding.accessBindingAdapter = AccessBindingAdapter
        observeViewModel(accessViewModel)
    }

    private fun observeViewModel(accessViewModel: AccessViewModel) {
        accessViewModel.registerStatus.observe(this.viewLifecycleOwner, Observer { accessStatus: RegisterStatus? ->
            when (accessStatus) {
                RegisterStatus.REGISTER_SUCCESSFULL -> {
                    Toasty.info(requireActivity(), "Register Successful!", Toasty.LENGTH_SHORT, true).show()
                    val args = Bundle()
                    args.putLong("uid", accessViewModel.getUid())
                    requireParentFragment().arguments = args
                    onRegisterSuccessful()
                }
                RegisterStatus.USER_ALREADY_EXISTS ->
                    Toasty.info(requireActivity(), "User already exists!", Toasty.LENGTH_SHORT, true).show()
            }
        })
    }

    private fun onRegisterSuccessful() {

    }

    private fun showDatePicker() {
        datePickerDialog.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        accessViewModel.onDateSelected(year, month, dayOfMonth)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dateselector -> showDatePicker()
            R.id.registerbutton -> accessViewModel.registerUser()
        }
    }
}