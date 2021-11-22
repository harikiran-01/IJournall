package com.hk.ijournal.views.access

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hk.ijournal.adapters.AccessBindingAdapter
import com.hk.ijournal.common.CommonLib.LOGTAG
import com.hk.ijournal.databinding.FragmentLoginBinding
import com.hk.ijournal.repository.AccessRepository.LoginStatus
import com.hk.ijournal.viewmodels.AccessViewModel
import com.hk.ijournal.viewmodels.RelayViewModel
import es.dmoral.toasty.Toasty

class LoginFragment : Fragment() {
    private var _loginBinding: FragmentLoginBinding? = null
    private val loginBinding get() = _loginBinding!!
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private val accessViewModel: AccessViewModel by viewModels(
        ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        loginBinding.lifecycleOwner = viewLifecycleOwner
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }

    override fun onDestroyView() {
        loginBinding.accessBindingAdapter = null
        loginBinding.accessViewModel = null
        loginBinding.unbind()
        _loginBinding = null
        super.onDestroyView()
        Log.d(LOGTAG, "LoginFrag onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOGTAG, "LoginFrag onDestroy")
    }
}