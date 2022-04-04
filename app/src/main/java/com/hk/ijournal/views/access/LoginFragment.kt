package com.hk.ijournal.views.access

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hk.ijournal.adapters.AccessBindingAdapter
import com.hk.ijournal.common.CommonLib.LOGTAG
import com.hk.ijournal.databinding.FragmentLoginBinding
import com.hk.ijournal.repository.AccessRepositoryImpl
import com.hk.ijournal.repository.AccessRepositoryImpl.AccessStatus
import com.hk.ijournal.repository.data.source.local.entities.User
import com.hk.ijournal.viewmodels.AccessViewModel
import com.hk.ijournal.viewmodels.RelayViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
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
        accessViewModel.loginStatus.observe(viewLifecycleOwner) { accessUser: AccessRepositoryImpl.AccessUser ->
            when (accessUser.accessStatus) {
                AccessStatus.LOGIN_SUCCESSFUL -> {
                    accessUser.diaryUser?.let { launchHomeOnSuccessfulAuth(it) }
                    Toasty.info(requireActivity(), "Login Successful!", Toasty.LENGTH_SHORT, true)
                        .show()
                }
                AccessStatus.INVALID_LOGIN -> {
                    Toasty.info(requireActivity(), "Invalid Password!", Toasty.LENGTH_SHORT, true)
                        .show()
                }
                AccessStatus.USER_NOT_FOUND -> {
                    Toasty.info(requireActivity(), "User doesn't exist!", Toasty.LENGTH_SHORT, true)
                        .show()
                }
            }
        }
    }

    private fun launchHomeOnSuccessfulAuth(diaryUser: User) {
        relayViewModel.onUserAuthorized(diaryUser)
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