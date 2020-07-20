package com.hk.ijournal.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.hk.ijournal.R
import com.hk.ijournal.databinding.ActivityLaunchBinding
import com.hk.ijournal.viewmodels.AccessViewModel

class LaunchActivity : AppCompatActivity() {
    private var debug: Boolean = false
    private lateinit var launchBinding: ActivityLaunchBinding
    private lateinit var accessFragment: Fragment

    //    @Override
    //    public void onBackPressed() {
    //        if (viewPager.getCurrentItem() == 0) {
    //            // If the user is currently looking at the first step, allow the system to handle the
    //            // Back button. This calls finish() on this activity and pops the back stack.
    //            super.onBackPressed();
    //        } else {
    //            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    //        }
    //    }
    private fun navigateTo(newFragment: Fragment, backStack: Boolean) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.view_fragment, newFragment)
        ft.commit()
        if (backStack) ft.addToBackStack("null")
    }

    fun launchHomeFragment() {
        navigateTo(HomeFragment(), false)
    }

    private fun launchAccessFragment() {
        accessFragment = AccessFragment()
        supportFragmentManager.beginTransaction().add(R.id.view_fragment, accessFragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lifecycle", "launchA onCreate")
        super.onCreate(savedInstanceState)
        launchBinding = DataBindingUtil.setContentView(this, R.layout.activity_launch)
        if (debug) launchHomeFragment() else launchAccessFragment()
    }

    companion object {
        @JvmStatic
        fun obtainViewModel(activity: FragmentActivity): AccessViewModel {
            return ViewModelProvider((activity as LaunchActivity).accessFragment).get(AccessViewModel::class.java)
        }
    }
}