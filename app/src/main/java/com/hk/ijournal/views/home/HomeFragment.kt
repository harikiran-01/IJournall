package com.hk.ijournal.views.home

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentHomeBinding
import com.hk.ijournal.viewmodels.HomeViewModel
import com.hk.ijournal.views.home.dashboard.DashboardFragment
import com.hk.ijournal.views.home.diary.DiaryFragment
import com.hk.ijournal.views.home.notifications.NotificationsFragment

class HomeFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance(userId: Long): HomeFragment {
            val homeFragment = HomeFragment()
            val args = Bundle()
            args.putLong("uid", userId)
            homeFragment.arguments = args
            return homeFragment
        }
    }

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        println("lifecycled homeF onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("lifecycled homeF onCreateView")
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView = fragmentHomeBinding.navView
        navView.setOnNavigationItemSelectedListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        addStartFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onDestroyView() {
        println("lifecycled homeF onDView")
        for (fragment in childFragmentManager.fragments) {
            childFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
        fragmentHomeBinding.unbind()
        homeViewModel.lastActiveFragTag = ""
        super.onDestroyView()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        loadFragment(item.title.toString())
        return true
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

    override fun onDestroy() {
        super.onDestroy()
        println("lifecycled homeF onDest")
    }
}