package com.bliss.auth.ui.views

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bliss.auth.databinding.FragmentRegisterBinding
import com.bliss.auth.repo.AccessRepositoryImpl
import com.bliss.auth.repo.User
import com.bliss.auth.ui.adapters.AccessBindingAdapter
import com.bliss.auth.viewmodels.AccessViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import omni.platform.android.shared.LogConstants.LOGTAG
import java.util.*

@AndroidEntryPoint
class RegisterFragment : Fragment(), OnDateSetListener {
    private lateinit var datePickerDialog: DatePickerDialog
    private var _registerBinding: FragmentRegisterBinding? = null
    private val registerBinding get() = _registerBinding!!
    private val accessViewModel: AccessViewModel by viewModels(
        ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _registerBinding = FragmentRegisterBinding.inflate(inflater, container, false)
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
        accessViewModel.registerStatus.observe(this.viewLifecycleOwner) { accessUser: AccessRepositoryImpl.AccessUser ->
            when (accessUser.accessStatus) {
                AccessRepositoryImpl.AccessStatus.REGISTER_SUCCESSFULL -> {
                    accessUser.diaryUser?.let { launchHomeOnSuccessfulAuth(it) }
                    Toasty.info(
                        requireActivity(),
                        "Register Successful!",
                        Toasty.LENGTH_SHORT,
                        true
                    ).show()
                }
                AccessRepositoryImpl.AccessStatus.USER_ALREADY_EXISTS ->
                    Toasty.info(
                        requireActivity(),
                        "User already exists!",
                        Toasty.LENGTH_SHORT,
                        true
                    ).show()
                else -> {}
            }
        }
    }

    fun showDatePicker() {
        datePickerDialog.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        accessViewModel.onDateSelected(year, month, dayOfMonth)
    }

    private fun launchHomeOnSuccessfulAuth(diaryUser: User) {
        //relayViewModel.onUserAuthorized(diaryUser)
    }

    override fun onDestroyView() {
        registerBinding.accessBindingAdapter = null
        registerBinding.accessViewModel = null
        registerBinding.registerFragment = null
        registerBinding.unbind()
        _registerBinding = null
        Log.d(LOGTAG, "RegisterFrag onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOGTAG, "RegisterFrag onDestroy")
    }
}