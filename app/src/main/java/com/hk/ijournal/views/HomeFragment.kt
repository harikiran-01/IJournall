package com.hk.ijournal.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return fragmentHomeBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle", "homeF onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView = fragmentHomeBinding.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_diary, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build()
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        //        NavigationUI.setupActionBarWithNavController((AppCompatActivity)requireActivity(), navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHomeBinding.unbind()
    }
}