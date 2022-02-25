package com.hk.ijournal.views


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.github.anrwatchdog.ANRWatchDog
import com.hk.ijournal.R
import com.hk.ijournal.databinding.ActivityLaunchBinding
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import com.hk.ijournal.utils.SessionAuthManager
import com.hk.ijournal.viewmodels.RelayViewModel
import com.hk.ijournal.views.access.AccessFragmentDirections
import com.hk.ijournal.views.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {
    private var launchBinding: ActivityLaunchBinding? = null
    private lateinit var addImageCallback: Observable.OnPropertyChangedCallback
    private lateinit var logoutCallback: Observable.OnPropertyChangedCallback
    private lateinit var navController: NavController
    private val relayViewModel by viewModels<RelayViewModel>()

    override fun onBackPressed() {
        finishAfterTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("lifecycled launchA onCreate")
        setTheme(R.style.AppTheme)
        ANRWatchDog().start()
        super.onCreate(savedInstanceState)
        launchBinding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(launchBinding?.root)
        //passing context to shared pref object
        SessionAuthManager.setContext(this)
        observeVM()
        navController = findNavController(R.id.nav_host_fragment)
        conditionalNavigate(navController)
    }

    private fun observeVM() {
        addImageCallback = relayViewModel.imagePickerClicked.observe { if (it.get()){
            openImagePicker()
            it.set(false)
            } }
        relayViewModel.onUserAuthorized.observe(this) { navigateFromAccessToHome(it) }
        logoutCallback = relayViewModel.onSessionEnd.observe { if (it.get()) {
            navigateFromHomeToAccess()
            it.set(false)
            } }
    }

    //extension inline higher order function to abstract "Observable" object's callback method
    private inline fun <reified T : Observable> T.observe(crossinline callback: (T) -> Unit) =
            object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(observable: Observable?, i: Int) =
                        callback(observable as T)
            }.also { addOnPropertyChangedCallback(it) }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RelayViewModel.RequestCode.IMAGEADDED.ordinal && resultCode == Activity.RESULT_OK && data != null) {
            //when single image is selected
            data.data?.let {
                relayViewModel.onImagesPicked(listOf(it.toString()))
            } ?:
            //when multiple images are selected
            data.clipData?.let {
                val imageUriListData = ArrayList<String>()
                for (pos in 0 until it.itemCount) {
                    imageUriListData.add(it.getItemAt(pos).uri.toString())
                }
                relayViewModel.onImagesPicked(imageUriListData)
            }
        }
    }

    @AfterPermissionGranted(1)
    private fun openImagePicker() {
        val perm = Manifest.permission.READ_EXTERNAL_STORAGE
        if (EasyPermissions.hasPermissions(this, perm)) {
            val imgIntent = Intent()
            imgIntent.type = "image/*"
            imgIntent.addCategory(Intent.CATEGORY_OPENABLE).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            imgIntent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(imgIntent, "Select Picture"), RelayViewModel.RequestCode.IMAGEADDED.ordinal)
        } else
            EasyPermissions.requestPermissions(this, "", 1, perm)

    }

    private fun conditionalNavigate(navController: NavController) {
        navController.graph.setStartDestination(if (SessionAuthManager.isUserLoggedIn()) R.id.home_dest else R.id.access_dest)
    }

//    private fun showAccessScreen() {
//        supportActionBar?.hide()
//        accessFragment = AccessFragment.newInstance()
//        supportFragmentManager.beginTransaction().add(R.id.main_nav_host, accessFragment!!, "access_frag").commitNow()
//    }
//
//    private fun showHomeScreen() {
//        supportActionBar?.show()
//        homeFragment = HomeFragment.newInstance(SessionAuthManager.getUID())
//        supportFragmentManager.beginTransaction().add(R.id.main_nav_host, homeFragment!!, "home_frag").commit()
//    }

    private fun navigateFromAccessToHome(diaryUser: DiaryUser) {
        supportActionBar?.show()
        with(diaryUser){
            SessionAuthManager.createUserLoginSession(diaryUser.uid)
            navController.navigate(AccessFragmentDirections.accessToHome(this))
        }
    }

    private fun navigateFromHomeToAccess() {
        supportActionBar?.hide()
        navController.navigate(HomeFragmentDirections.homeToAccess())
    }

    override fun onDestroy() {
        println("lifecycled launchA onDestroy")
        relayViewModel.imagePickerClicked.removeOnPropertyChangedCallback(addImageCallback)
        relayViewModel.onSessionEnd.removeOnPropertyChangedCallback(logoutCallback)
        super.onDestroy()
    }
}