package com.hk.ijournal.views


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.hk.ijournal.R
import com.hk.ijournal.views.home.HomeFragment

class LaunchActivity : AppCompatActivity() {
    var loginstatus: Boolean = false

    //private var accessFragment : AccessFragment? = null
    private var homeFragment: HomeFragment? = null

    override fun onBackPressed() {
        finishAfterTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("lifecycle", "launchA onCreate")
        super.onCreate(savedInstanceState)
        setTheme(R.style.HomeTheme)
        conditionalNavigate()
        setContentView(R.layout.activity_launch)
        val navController = findNavController(R.id.main_nav_host)
        val graph = navController.navInflater.inflate(R.navigation.app_navigation)
        graph.startDestination = if (loginstatus) R.id.home_dest else R.id.access_dest
        navController.graph = graph
    }

    private fun conditionalNavigate() {

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifecycle", "launchA onDestroy")
    }
}