package omni.platform.android.components.android

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus

abstract class BaseFragment(@LayoutRes layoutId: Int = 0) : Fragment(layoutId) {
    protected open val lifecycleScopeSafe by lazy {
        lifecycleScope + coroutineExceptionHandler
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

    }
}