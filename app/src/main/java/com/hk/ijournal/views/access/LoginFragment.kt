package com.hk.ijournal.views.access

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hk.ijournal.R
import com.hk.ijournal.adapters.AccessBindingAdapter
import com.hk.ijournal.databinding.FragmentLoginBinding
import com.hk.ijournal.repository.AccessRepository.LoginStatus
import com.hk.ijournal.viewmodels.AccessViewModel
import com.hk.ijournal.viewmodels.RelayViewModel
import es.dmoral.toasty.Toasty

class LoginFragment : Fragment() {
    private lateinit var accessViewModel: AccessViewModel
    private lateinit var loginBinding: FragmentLoginBinding
    private val relayViewModel by activityViewModels<RelayViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return loginBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        accessViewModel = ViewModelProvider(requireParentFragment()).get(AccessViewModel::class.java)
        loginBinding.lifecycleOwner = viewLifecycleOwner
        loginBinding.accessBindingAdapter = AccessBindingAdapter
        loginBinding.accessViewModel = accessViewModel
        observeViewModel()
    }

    private fun observeViewModel() {
        accessViewModel.loginStatus.observe(viewLifecycleOwner, Observer { accessStatus: LoginStatus ->
            when (accessStatus) {
                LoginStatus.LOGIN_SUCCESSFUL -> {
                    launchHomeOnAccessValidation()
                    Toasty.info(requireActivity(), "Login Successful!", Toasty.LENGTH_SHORT, true).show()
                }
                LoginStatus.INVALID_LOGIN -> {
                    Toasty.info(requireActivity(), "Invalid Password!", Toasty.LENGTH_SHORT, true).show()
                }
                LoginStatus.USER_NOT_FOUND -> {
                    Toasty.info(requireActivity(), "User doesn't exist!", Toasty.LENGTH_SHORT, true).show()
                }
            }
        })

    }

    private fun launchHomeOnAccessValidation() {
        val args = Bundle()
        args.putLong("uid", accessViewModel.getUid())
        requireParentFragment().arguments = args
        relayViewModel.isAccessAuthorized.set(true)
        relayViewModel.isAccessAuthorized.set(false)
    }

}