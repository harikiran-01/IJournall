package com.hk.ijournal.views


import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bliss.auth.repo.User
import com.hk.ijournal.R
import com.hk.ijournal.databinding.ActivityLaunchBinding
import com.hk.ijournal.utils.SessionAuthManager
import com.hk.ijournal.viewmodels.RelayViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {
    private var launchBinding: ActivityLaunchBinding? = null
    private lateinit var addImageCallback: Observable.OnPropertyChangedCallback
    private lateinit var logoutCallback: Observable.OnPropertyChangedCallback
    private val relayViewModel by viewModels<RelayViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchBinding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(launchBinding?.root)
        //passing context to shared pref object
        SessionAuthManager.setContext(this)
        observeVM()
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_app) as NavHostFragment
        navController = navHostFragment.navController
        conditionalNavigate()
    }

    private fun observeVM() {
        addImageCallback = relayViewModel.imagePickerClicked.observe { if (it.get()){
            openImagePicker()
            it.set(false)
            } }
        relayViewModel.onUserAuthorized.observe(this) {
            createUserSession(it)
            navigateFromAccessToLanding(it) }
        logoutCallback = relayViewModel.onSessionEnd.observe { if (it.get()) {
            navigateFromLandingToAccess()
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

                val mimeType = with(it.toString()) {
                    when {
                        contains("image") -> "image"
                        contains("mp4") -> "video"
                        else -> "image"
                    }
                }
                relayViewModel.onImagesPicked(listOf(it.toString() to mimeType))
            } ?:
            //when multiple images are selected
            data.clipData?.let {
                val imageUriListData = ArrayList<Pair<String, String>>()
                for (pos in 0 until it.itemCount) {
                    val mimeType = with(it.getItemAt(pos).uri.toString()) {
                        when {
                            contains("image") -> "image"
                            contains("video") -> "video"
                            else -> "image"
                        }
                    }
                    imageUriListData.add(it.getItemAt(pos).uri.toString() to mimeType)
                }
                relayViewModel.onImagesPicked(imageUriListData)
            }
        }


    }

    @AfterPermissionGranted(1)
    private fun openImagePicker() {
        val perm = Manifest.permission.READ_EXTERNAL_STORAGE
        if (EasyPermissions.hasPermissions(this, perm)) {
            val imgIntent = Intent(Intent.ACTION_GET_CONTENT).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            imgIntent.type = "image/*"
            //imgIntent.addCategory(Intent.CATEGORY_OPENABLE).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            //imgIntent.action = Intent.ACTION_OPEN_DOCUMENT
            try {
                startActivityForResult(Intent.createChooser(imgIntent, "Select Picture"), RelayViewModel.RequestCode.IMAGEADDED.ordinal)
            }
            catch (e: ActivityNotFoundException) {
                //no ops
            }

        } else
            EasyPermissions.requestPermissions(this, "", 1, perm)
    }

    private fun conditionalNavigate() {
        if (true) {
            /*val diaryUser = relayViewModel.getUser(SessionAuthManager.getUID())*/
//            navigateFromAccessToLanding(diaryUser?: User())
        }
    }

    private fun createUserSession(diaryUser: User) {
        SessionAuthManager.createUserLoginSession(0)
    }

    private fun navigateFromAccessToLanding(diaryUser: User) {
        // replace fragment in root
    }

    private fun navigateFromLandingToAccess() {
//        SessionAuthManager.logoutUser()
//        navController.navigate(AccessFragmentDirections.actionGlobalAccessDest())
    }

    override fun onDestroy() {
        println("lifecycled launchA onDestroy")
        relayViewModel.imagePickerClicked.removeOnPropertyChangedCallback(addImageCallback)
        relayViewModel.onSessionEnd.removeOnPropertyChangedCallback(logoutCallback)
        super.onDestroy()
    }
}