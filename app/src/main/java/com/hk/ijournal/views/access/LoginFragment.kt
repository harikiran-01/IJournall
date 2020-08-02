package com.hk.ijournal.views.access

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hk.ijournal.R
import com.hk.ijournal.adapters.AccessBindingAdapter
import com.hk.ijournal.databinding.FragmentLoginBinding
import com.hk.ijournal.repository.AccessRepository.LoginStatus
import com.hk.ijournal.viewmodels.AccessViewModel
import es.dmoral.toasty.Toasty

class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var accessViewModel: AccessViewModel
    private lateinit var loginBinding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d("lifecycle", "loginF onCreateView")
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        loginBinding.loginbutton.setOnClickListener(this)
        loginBinding.lifecycleOwner = viewLifecycleOwner
        loginBinding.accessBindingAdapter = AccessBindingAdapter
        return loginBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        accessViewModel = ViewModelProvider(requireParentFragment()).get(AccessViewModel::class.java)
        Log.d("lifecycle", "loginF onActivityCreated")
        loginBinding.accessViewModel = accessViewModel
        observeViewModel()
    }

    private fun observeViewModel() {
        accessViewModel.loginStatus.observe(viewLifecycleOwner, Observer { accessStatus: LoginStatus ->
            Log.d("lifecycle", "loginF observeViewModel")
            when (accessStatus) {
                LoginStatus.LOGIN_SUCCESSFUL -> {
                    Toasty.info(requireActivity(), "Login Successful!", Toasty.LENGTH_SHORT, true).show()
                    onLoginSuccessful()
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

    private fun onLoginSuccessful() {
        findNavController().navigate(AccessFragmentDirections.accessToHome(accessViewModel.getUid()))
    }

    override fun onClick(v: View) {
        if (v.id == R.id.loginbutton) {
            accessViewModel.loginUser()
        }
    }
}