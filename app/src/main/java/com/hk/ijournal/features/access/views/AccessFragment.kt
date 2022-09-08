package com.hk.ijournal.features.access.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import bliss.platform.android.shared.LogConstants.LOGTAG
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentAccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccessFragment : Fragment() {

    companion object {
        fun newInstance(): AccessFragment {
            return AccessFragment()
        }
    }

    @StringRes
    private val tabTitles = intArrayOf(R.string.login_tab_text, R.string.register_tab_text)

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private var viewPager: ViewPager2? = null

    private var _accessBinding: FragmentAccessBinding? = null
    private val accessBinding get() = _accessBinding!!

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var pagerAdapter: FragmentStateAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _accessBinding = FragmentAccessBinding.inflate(inflater, container, false)
        return accessBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = accessBinding.pager
        pagerAdapter = ScreenSlidePagerAdapter(childFragmentManager, lifecycle)
        viewPager!!.adapter = pagerAdapter
        accessBinding.lifecycleOwner = this
        val tabLayout = accessBinding.accessTabs
        TabLayoutMediator(tabLayout, viewPager!!,
                TabConfigurationStrategy { tab: TabLayout.Tab, position: Int -> tab.setText(tabTitles[position]) }
        ).attach()
    }

    override fun onDestroyView() {
        Log.d(LOGTAG, "AccessFragment onDestroyView")
        pagerAdapter = null
        viewPager?.adapter = null
        viewPager = null
        accessBinding.unbind()
        _accessBinding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOGTAG, "AccessFragment onDestroy")
    }

    private class ScreenSlidePagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fm, lifecycle) {
        // The number of tabs.
        private val tabCount = 2
        override fun createFragment(position: Int): Fragment {
            return if (position == 0)
                LoginFragment()
            else RegisterFragment()
        }

        override fun getItemCount(): Int {
            return tabCount
        }
    }
}