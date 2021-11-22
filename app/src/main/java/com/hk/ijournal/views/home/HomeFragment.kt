package com.hk.ijournal.views.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.navigation.NavigationBarView
import com.hk.ijournal.R
import com.hk.ijournal.common.CommonLib.LOGTAG
import com.hk.ijournal.databinding.FragmentHomeBinding
import com.hk.ijournal.viewmodels.HomeViewModel
import com.hk.ijournal.views.home.dashboard.DashboardFragment
import com.hk.ijournal.views.home.diary.DiaryFragment
import com.hk.ijournal.views.home.notifications.NotificationsFragment

class HomeFragment : Fragment() {

    companion object {
        fun newInstance(userId: Long): HomeFragment {
            val homeFragment = HomeFragment()
            val args = Bundle()
            args.putLong("uid", userId)
            homeFragment.arguments = args
            return homeFragment
        }
    }

    private val homeViewModel: HomeViewModel by viewModels()
    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding get() = _fragmentHomeBinding!!
    private lateinit var onItemSelectedListener: NavigationBarView.OnItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        onItemSelectedListener = NavigationBarView.OnItemSelectedListener {
            loadFragment(it.title.toString())
            true
        }
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
        navView.setOnItemSelectedListener(onItemSelectedListener)
        addStartFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
    }

    private fun addStartFragment() {
        val userId = requireArguments().getLong("uid")
        childFragmentManager.beginTransaction().add(R.id.nav_container, DiaryFragment.newInstance(userId), "Diary").commit()
        homeViewModel.lastActiveFragTag = "Diary"
    }

    private fun loadFragment(itemTitle: String) {
        val userId = requireArguments().getLong("uid")
        val fragment = childFragmentManager.findFragmentByTag(itemTitle) ?: when (itemTitle) {
            "Diary" -> {
                Log.d(LOGTAG, "DiaryFragNewInstance")
                DiaryFragment.newInstance(userId)
            }
            "Dashboard" -> {
                DashboardFragment()
            }
            "Notifications" -> {
                NotificationsFragment()
            }
            else -> {
                null
            }
        }

        // show/hide fragment
        if (fragment != null) {
            val transaction = childFragmentManager.beginTransaction()

            if (homeViewModel.lastActiveFragTag != "") {
                val lastFragment = childFragmentManager.findFragmentByTag(homeViewModel.lastActiveFragTag)
                if (lastFragment != null)
                    transaction.hide(lastFragment)
            }

            if (!fragment.isAdded) {
                transaction.add(R.id.nav_container, fragment, itemTitle)
            } else {
                transaction.show(fragment)
            }

            transaction.commit()
            homeViewModel.lastActiveFragTag = itemTitle
        }
    }

    override fun onDestroyView() {
        println("lifecycled homeF onDView")
        for (fragment in childFragmentManager.fragments) {
            childFragmentManager.beginTransaction().remove(fragment).commitNow()
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