package omni.platform.android.components.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus

abstract class BaseViewModel : ViewModel() {
    protected open val viewModelScopeSafe by lazy {
        viewModelScope + coroutineExceptionHandler
    }

    open val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

    }
}