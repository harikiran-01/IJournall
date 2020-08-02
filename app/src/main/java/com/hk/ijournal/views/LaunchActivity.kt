package com.hk.ijournal.views


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hk.ijournal.R
import com.hk.ijournal.views.access.AccessFragment
import com.hk.ijournal.views.home.HomeFragment

class LaunchActivity : AppCompatActivity() {
    var loginstatus: Boolean = false
    private var accessFragment: AccessFragment? = null
    private var homeFragment: HomeFragment? = null

    override fun onBackPressed() {
        finishAfterTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lifecycle", "launchA onCreate")
        setTheme(R.style.HomeTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        conditionalNavigate()
    }


    private fun conditionalNavigate() {
        if (loginstatus) showHomeScreen() else showAccessScreen()
    }

    private fun showAccessScreen() {
        accessFragment = AccessFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.main_nav_host, accessFragment!!, "access_frag").commitNow()
        accessFragment!!.registerOnAccessPassListener {
            navigateFromAccessToHome()
        }
    }

    private fun showHomeScreen() {
        homeFragment = HomeFragment.newInstance(0)
        supportFragmentManager.beginTransaction().add(R.id.main_nav_host, homeFragment!!, "home_frag").commit()
    }

    private fun navigateFromAccessToHome() {
        homeFragment = HomeFragment.newInstance(accessFragment!!.requireArguments().getLong("uid"))
        accessFragment = null
        supportFragmentManager.beginTransaction().replace(R.id.main_nav_host, homeFragment!!, "home_frag").commit()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifecycle", "launchA onDestroy")
    }
}