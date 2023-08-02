package omni.platform.android.components.android

import androidx.fragment.app.Fragment

/**
 * Config to customize behaviour of [BaseActivity]
 */
open class BaseActivityConfig(
    /**
     * Provides a fragment to be
     */
    var fragmentProvider: (() -> Fragment)? = null,

    var immersiveExperience: Boolean = true
)