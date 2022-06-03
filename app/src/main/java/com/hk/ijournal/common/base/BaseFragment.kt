package com.hk.ijournal.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, VM : ViewModel> : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!
    private var hasInitializedRootView = false

    protected lateinit var viewModel: VM

    protected abstract fun getViewModelClass(): Class<VM>?

    protected abstract fun getViewBinding(): B

    protected open val useSharedViewModel = false

    protected open var keepBindingRetained = false

    /**
     * Use to inject dagger as dagger cannot be injected into generic classes
     */
    protected open fun inject() {}

    /**
     * Use to receive extras from fragment's arguments
     */
    protected open fun receiveExtras() {}

    /**
     *
     */
    protected open fun onViewLifecycleStart() {}

    /**
     *
     */
    protected open fun onLifecycleStart() {}

    /**
     * Only use to create/initialise views
     */
    protected open fun setUpViews() {}

    /**
     * Only use to setup listeners
     */
    protected open fun setUpListeners() {}

    /**
     * Only use to register observers
     */
    protected open fun observeData() {}

    /**
     * This function can be called in case to clear any view or listener that are attached to views owned by fragment or viewLifecycleObservers in fragment
     */
    protected open fun doViewCleanup() {}

    /**
     * This function can be called in case to clear any resource hold by fragment that should not live out of the fragment lifecycle
     */
    protected open fun doCleanup() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onLifecycleStart()
        inject()
        init()
        receiveExtras()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onViewLifecycleStart()
        if (_binding == null) {
            _binding = getViewBinding()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView && keepBindingRetained) {
            hasInitializedRootView = true
            setUpViews()
            setUpListeners()
        } else if (!keepBindingRetained) {
            setUpViews()
            setUpListeners()
        }
        observeData()
    }

    override fun onDestroyView() {
        doViewCleanup()
        if (!keepBindingRetained) {
            _binding = null
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        doCleanup()
        super.onDestroy()
    }

    private fun init() {
        if (getViewModelClass() == null) {
            return
        }
        viewModel = if (useSharedViewModel) {
            ViewModelProvider(requireActivity()).get(getViewModelClass()!!)
        } else {
            ViewModelProvider(this).get(getViewModelClass()!!)
        }
    }

    protected fun isBindingInitialized(): Boolean {
        return _binding != null
    }

}