package omni.platform.android.components.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    open val config : BaseActivityConfig = BaseActivityConfig()

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected lateinit var viewModel: VM

    protected abstract fun getViewModelClass(): Class<VM>?

    protected abstract fun getViewBinding(): VB

    /**
     * Use to inject dagger as dagger cannot be injected into generic classes
     */
    protected open fun inject() {}

    /**
     *
     */
    protected open fun onLifecycleStart() {}

    /**
     * Setup the view for the activity
     */
    protected open fun setupView() {}

    /**
     * Only use to setup listeners
     */
    protected open fun setUpListeners() {}

    /**
     * Only use to register observers
     */
    protected open fun observeData() {}

    /**
     * This function can be called in case to clear any resource held by activity that should not live out of the activity lifecycle
     */
    protected open fun doCleanup() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onLifecycleStart()
        inject()
        init()
        _binding = getViewBinding()
        _binding?.let { setContentView(it.root) }
        setupView()
        setUpListeners()
        observeData()
    }

    override fun onDestroy() {
        doCleanup()
        super.onDestroy()
    }

    private fun init() {
        getViewModelClass()?.let { viewModel = ViewModelProvider(this)[it] } ?: return
    }

    protected fun isBindingInitialized(): Boolean {
        return _binding != null
    }

}