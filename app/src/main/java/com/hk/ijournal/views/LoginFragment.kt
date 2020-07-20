package com.hk.ijournal.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hk.ijournal.R
import com.hk.ijournal.adapters.AccessBindingAdapter
import com.hk.ijournal.databinding.FragmentLoginBinding
import com.hk.ijournal.repository.models.AccessModel.AccessStatus
import com.hk.ijournal.viewmodels.AccessViewModel
import com.hk.ijournal.views.LaunchActivity.Companion.obtainViewModel
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
        Log.d("lifecycle", "loginF onActivityCreated")
        accessViewModel = obtainViewModel(requireActivity())
        loginBinding.accessViewModel = accessViewModel
        observeViewModel(accessViewModel)
    }

    private fun observeViewModel(accessViewModel: AccessViewModel) {
        accessViewModel.accessStatus.observe(viewLifecycleOwner, Observer { accessStatus: AccessStatus ->
            Log.d("lifecycle", "loginF observeViewModel")
            when (accessStatus) {
                AccessStatus.LOGIN_SUCCESSFUL -> {
                    Toasty.info(requireActivity(), "Login Successful!", Toasty.LENGTH_SHORT, true).show()
                    onLoginSuccessful()
                }
                AccessStatus.INVALID_LOGIN -> {
                    Toasty.info(requireActivity(), "Invalid Password!", Toasty.LENGTH_SHORT, true).show()
                }
                AccessStatus.USER_NOT_FOUND -> {
                    Toasty.info(requireActivity(), "User doesn't exist!", Toasty.LENGTH_SHORT, true).show()
                }
            }
        })
    }

    private fun onLoginSuccessful() {
        (requireActivity() as LaunchActivity).launchHomeFragment()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.loginbutton) {
            accessViewModel.loginUser()
        }
    }
}