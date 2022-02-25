package com.hk.ijournal.views.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgument
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationBarView
import com.hk.ijournal.R
import com.hk.ijournal.common.CommonLib.LOGTAG
import com.hk.ijournal.common.Constants
import com.hk.ijournal.databinding.FragmentHomeBinding
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import com.hk.ijournal.utils.SessionAuthManager
import com.hk.ijournal.viewmodels.HomeViewModel
import com.hk.ijournal.views.home.dashboard.DashboardFragment
import com.hk.ijournal.views.home.diary.DiaryFragment
import com.hk.ijournal.views.home.notifications.NotificationsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val safeArgs: HomeFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModels()
    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding get() = _fragmentHomeBinding!!
    private lateinit var onItemSelectedListener: NavigationBarView.OnItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        onItemSelectedListener = NavigationBarView.OnItemSelectedListener {
//            loadFragment(it.title.toString())
//            true
//        }
        println("lifecycled homeF onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        println("lifecycled homeF onCreateView")
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView = fragmentHomeBinding.navView
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_home)
        val graph = navController.navInflater.inflate(R.navigation.home_navigation)
        graph.setStartDestination(R.id.navigation_diary)
        val diaryUserArg = NavArgument.Builder().setDefaultValue(safeArgs.diaryUser).build()
        graph.addArgument(Constants.DIARY_USER, diaryUserArg)
        navController.graph = graph
        NavigationUI.setupWithNavController(navView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
    }

//    private fun addStartFragment() {
//        childFragmentManager.beginTransaction().add(R.id.nav_container, DiaryFragment.newInstance(safeArgs.diaryUser), "Diary").commit()
//        homeViewModel.lastActiveFragTag = "Diary"
//    }

//    private fun loadFragment(itemTitle: String) {
//        val fragment = childFragmentManager.findFragmentByTag(itemTitle) ?: when (itemTitle) {
//            "Diary" -> {
//                Log.d(LOGTAG, "DiaryFragNewInstance")
//                DiaryFragment.newInstance(safeArgs.diaryUser)
//            }
//            "Dashboard" -> {
//                DashboardFragment()
//            }
//            "Notifications" -> {
//                NotificationsFragment()
//            }
//            else -> {
//                null
//            }
//        }
//
//        // show/hide fragment
//        if (fragment != null) {
//            val transaction = childFragmentManager.beginTransaction()
//
//            if (homeViewModel.lastActiveFragTag != "") {
//                val lastFragment = childFragmentManager.findFragmentByTag(homeViewModel.lastActiveFragTag)
//                if (lastFragment != null)
//                    transaction.hide(lastFragment)
//            }
//
//            if (!fragment.isAdded) {
//                transaction.add(R.id.nav_container, fragment, itemTitle)
//            } else {
//                transaction.show(fragment)
//            }
//
//            transaction.commit()
//            homeViewModel.lastActiveFragTag = itemTitle
//        }
//    }

    override fun onDestroyView() {
        println("lifecycled homeF onDView")
        for (fragment in childFragmentManager.fragments) {
            childFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
        fragmentHomeBinding.navView.setOnItemSelectedListener(null)
        fragmentHomeBinding.unbind()
        _fragmentHomeBinding = null
        homeViewModel.lastActiveFragTag = ""
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("lifecycled homeF onDest")
    }
}