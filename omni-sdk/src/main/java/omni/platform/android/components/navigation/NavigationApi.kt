package omni.platform.android.components.navigation

import android.content.Context
import androidx.fragment.app.Fragment
import omni.platform.api.PlatformApi

interface NavigationApi : PlatformApi {
    fun addFragment(context: Context, fragment: Fragment)

    fun replaceFragment(fragment: Fragment)
}