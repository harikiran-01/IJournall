package com.bliss.auth.ui.views

import androidx.fragment.app.viewModels
import com.bliss.auth.databinding.FragmentLoginBinding
import com.bliss.auth.repo.AccessRepositoryImpl
import com.bliss.auth.repo.User
import com.bliss.auth.ui.AuthBaseFragment
import com.bliss.auth.viewmodels.AccessViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class LoginFragment : AuthBaseFragment<FragmentLoginBinding, Nothing>() {

    override fun getViewModelClass(): Class<Nothing>? {
        return null
    }

    override fun getViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    private val accessViewModel: AccessViewModel by viewModels(
        ownerProducer = { requireParentFragment() })

    override fun setUpViews() {
        super.setUpViews()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.accessViewModel = accessViewModel
    }

    override fun observeData() {
        super.observeData()
        accessViewModel.loginStatus.observe(viewLifecycleOwner) { accessUser: AccessRepositoryImpl.AccessUser ->
            when (accessUser.accessStatus) {
                AccessRepositoryImpl.AccessStatus.LOGIN_SUCCESSFUL -> {
                    accessUser.diaryUser?.let { launchHomeOnSuccessfulAuth(it) }
                    Toasty.info(requireActivity(), "Login Successful!", Toasty.LENGTH_SHORT, true)
                        .show()
                }
                AccessRepositoryImpl.AccessStatus.INVALID_LOGIN -> {
                    Toasty.info(requireActivity(), "Invalid Password!", Toasty.LENGTH_SHORT, true)
                        .show()
                }
                AccessRepositoryImpl.AccessStatus.USER_NOT_FOUND -> {
                    Toasty.info(requireActivity(), "User doesn't exist!", Toasty.LENGTH_SHORT, true)
                        .show()
                }
                else -> {

                }
            }
        }
    }

    private fun launchHomeOnSuccessfulAuth(diaryUser: User) {
        //relayViewModel.onUserAuthorized(diaryUser)
    }

    override fun doViewCleanup() {
        super.doViewCleanup()
        binding.accessBindingAdapter = null
        binding.accessViewModel = null
    }
}