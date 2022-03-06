package com.hk.ijournal.landing.views

import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.hk.ijournal.R
import com.hk.ijournal.common.Constants
import com.hk.ijournal.common.base.BaseFragment
import com.hk.ijournal.databinding.LandingFragmentBinding
import com.hk.ijournal.feed.viewmodel.LandingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LandingFragment : BaseFragment<LandingFragmentBinding, LandingViewModel>() {
    private val safeArgs: LandingFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun getViewModelClass(): Class<LandingViewModel> {
        return LandingViewModel::class.java
    }

    override fun getViewBinding(): LandingFragmentBinding {
        return LandingFragmentBinding.inflate(layoutInflater)
    }

    override fun setUpViews() {
        super.setUpViews()
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_landing)
        val graph = navController.navInflater.inflate(R.navigation.landing_navigation)
        graph.setStartDestination(R.id.feed_dest)
        val diaryUserArg = NavArgument.Builder().setDefaultValue(safeArgs.diaryUser).build()
        graph.addArgument(Constants.DIARY_USER, diaryUserArg)
        navController.graph = graph
    }

}