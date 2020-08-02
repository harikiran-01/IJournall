package com.hk.ijournal.views.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentHomeBinding
import com.hk.ijournal.viewmodels.HomeViewModel
import com.hk.ijournal.views.dashboard.DashboardFragment
import com.hk.ijournal.views.diary.DiaryFragment
import com.hk.ijournal.views.notifications.NotificationsFragment

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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifecycle", "homeF onDest")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("lifecycle", "homeF onCreateView")
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        return fragmentHomeBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(fragmentHomeBinding.appBar)
        val navView = fragmentHomeBinding.navView
        navView.setOnNavigationItemSelectedListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        addStartFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle", "homeF onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onDestroyView() {
        Log.d("lifecycle", "homeF onDView")
        super.onDestroyView()
        fragmentHomeBinding.unbind()
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        loadFragment(item.title.toString())
        return true
    }

    private fun addStartFragment() {
        println("navideb addstart")
        val userId = requireArguments().getLong("uid")
        childFragmentManager.beginTransaction().add(R.id.nav_container, DiaryFragment.newInstance(userId), "Diary").commit()
        homeViewModel.lastActiveFragmentTag = "Diary"
    }

    private fun loadFragment(itemTitle: String) {
        val userId = requireArguments().getLong("uid")
        println("navideb tag $itemTitle")
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

            if (homeViewModel.lastActiveFragmentTag != "") {
                val lastFragment = childFragmentManager.findFragmentByTag(homeViewModel.lastActiveFragmentTag)
                if (lastFragment != null)
                    transaction.hide(lastFragment)
            }

            if (!fragment.isAdded) {
                println("navideb added $fragment")
                transaction.add(R.id.nav_container, fragment, itemTitle)
            } else {
                transaction.show(fragment)
            }

            transaction.commit()
            homeViewModel.lastActiveFragmentTag = itemTitle
        }
    }


}