package com.hk.ijournal.views


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import com.github.anrwatchdog.ANRWatchDog
import com.hk.ijournal.R
import com.hk.ijournal.utils.SessionAuthManager
import com.hk.ijournal.viewmodels.RelayViewModel
import com.hk.ijournal.views.access.AccessFragment
import com.hk.ijournal.views.home.HomeFragment
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class LaunchActivity : AppCompatActivity() {
    private var accessFragment: AccessFragment? = null
    private var homeFragment: HomeFragment? = null
    private lateinit var addImageCallback: Observable.OnPropertyChangedCallback
    private lateinit var accessAuthCallback: Observable.OnPropertyChangedCallback
    private lateinit var logoutCallback: Observable.OnPropertyChangedCallback
    private val relayViewModel by viewModels<RelayViewModel>()

    override fun onBackPressed() {
        finishAfterTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("lifecycled launchA onCreate")
        setTheme(R.style.AppTheme)
        ANRWatchDog().start()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        //passing context to shared pref object
        SessionAuthManager.setContext(this)
        observeVM()
        conditionalNavigate()
    }

    private fun observeVM() {
        addImageCallback = relayViewModel.imagePickerClicked.observe { if (it.get()){
            openImagePicker()
            it.set(false)
            } }
        accessAuthCallback = relayViewModel.isAccessAuthorized.observe { if (it.get()) {
            navigateFromAccessToHome()
            it.set(false)
            } }
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
            data.data?.run {
                val args = Bundle()
                val imageuridata = ArrayList<String>()
                toString().let { imageuridata.add(it) }
                args.putStringArrayList("imageuridata", imageuridata)
                homeFragment?.arguments = args
                relayViewModel.onImagePicked()
            } ?:
            //when multiple images are selected
            data.clipData?.let {
                val args = Bundle()
                val imageUriListData = ArrayList<String>()
                for (pos in 0 until it.itemCount) {
                    imageUriListData.add(it.getItemAt(pos).uri.toString())
                }
                args.putStringArrayList("imageuridata", imageUriListData)
                homeFragment?.arguments = args
                relayViewModel.onImagePicked()
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

    private fun conditionalNavigate() {
        if (SessionAuthManager.isUserLoggedIn()) showHomeScreen() else showAccessScreen()
    }

    private fun showAccessScreen() {
        supportActionBar?.hide()
        accessFragment = AccessFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.main_nav_host, accessFragment!!, "access_frag").commitNow()
    }

    private fun showHomeScreen() {
        supportActionBar?.show()
        homeFragment = HomeFragment.newInstance(SessionAuthManager.getUID())
        supportFragmentManager.beginTransaction().add(R.id.main_nav_host, homeFragment!!, "home_frag").commit()
    }

    private fun navigateFromAccessToHome() {
        supportActionBar?.show()
        val newUserId = accessFragment!!.requireArguments().getLong("uid")
        SessionAuthManager.createUserLoginSession(newUserId)
        homeFragment = HomeFragment.newInstance(newUserId)
        print("bugbash is access child destroyed ${accessFragment?.childFragmentManager?.isDestroyed}")
        accessFragment = null
        supportFragmentManager.beginTransaction().replace(R.id.main_nav_host, homeFragment!!, "home_frag").commit()
    }

    private fun navigateFromHomeToAccess() {
        supportActionBar?.hide()
        accessFragment = AccessFragment.newInstance()
        homeFragment = null
        supportFragmentManager.beginTransaction().replace(R.id.main_nav_host, accessFragment!!, "access_frag").commit()
    }

    override fun onDestroy() {
        println("lifecycled launchA onDestroy")
        relayViewModel.imagePickerClicked.removeOnPropertyChangedCallback(addImageCallback)
        relayViewModel.isAccessAuthorized.removeOnPropertyChangedCallback(accessAuthCallback)
        relayViewModel.onSessionEnd.removeOnPropertyChangedCallback(logoutCallback)
        super.onDestroy()
    }
}