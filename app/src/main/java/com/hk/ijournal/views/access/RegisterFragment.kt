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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hk.ijournal.R
import com.hk.ijournal.adapters.AccessBindingAdapter
import com.hk.ijournal.databinding.FragmentRegisterBinding
import com.hk.ijournal.repository.AccessRepository.RegisterStatus
import com.hk.ijournal.viewmodels.AccessViewModel
import com.hk.ijournal.viewmodels.RelayViewModel
import es.dmoral.toasty.Toasty
import java.util.*

class RegisterFragment : Fragment(), OnDateSetListener {
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var registerBinding: FragmentRegisterBinding
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private val accessViewModel: AccessViewModel by viewModels(
        ownerProducer = { requireParentFragment() })
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false)
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
        registerBinding.lifecycleOwner = viewLifecycleOwner
        registerBinding.accessViewModel = accessViewModel
        registerBinding.registerFragment = this
        registerBinding.accessBindingAdapter = AccessBindingAdapter
        observeViewModel()
    }

    private fun observeViewModel() {
        accessViewModel.registerStatus.observe(this.viewLifecycleOwner, Observer { accessStatus: RegisterStatus? ->
            when (accessStatus) {
                RegisterStatus.REGISTER_SUCCESSFULL -> {
                    launchHomeOnAccessValidation()
                    Toasty.info(requireActivity(), "Register Successful!", Toasty.LENGTH_SHORT, true).show()
                }
                RegisterStatus.USER_ALREADY_EXISTS ->
                    Toasty.info(requireActivity(), "User already exists!", Toasty.LENGTH_SHORT, true).show()
            }
        })
    }

    fun showDatePicker() {
        datePickerDialog.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        accessViewModel.onDateSelected(year, month, dayOfMonth)
    }

    private fun launchHomeOnAccessValidation() {
        val args = Bundle()
        args.putLong("uid", accessViewModel.getUid())
        requireParentFragment().arguments = args
        relayViewModel.isAccessAuthorized.set(true)
    }

}