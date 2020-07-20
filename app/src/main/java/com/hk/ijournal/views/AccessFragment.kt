package com.hk.ijournal.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentAccessBinding

class AccessFragment : Fragment() {
    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.login_tab_text, R.string.register_tab_text)

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    private lateinit var accessBinding: FragmentAccessBinding

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var pagerAdapter: FragmentStateAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle", "accessF onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        accessBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_access, container, false)
        return accessBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = accessBinding.pager
        pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter
        accessBinding.lifecycleOwner = this
        val tabLayout = accessBinding.accessTabs
        val dotTabs = accessBinding.tabsDots
        TabLayoutMediator(tabLayout, viewPager,
                TabConfigurationStrategy { tab: TabLayout.Tab, position: Int -> tab.setText(TAB_TITLES[position]) }
        ).attach()
        TabLayoutMediator(dotTabs, viewPager,
                TabConfigurationStrategy { tab: TabLayout.Tab, position: Int -> }
        ).attach()

    }

    private class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        // The number of tabs.
        private val NUM_TABS = 2
        override fun createFragment(position: Int): Fragment {
            Log.d("lifecycle", "viewpager createFragment")
            return if (position == 0) LoginFragment() else RegisterFragment()
        }

        override fun getItemCount(): Int {
            return NUM_TABS
        }
    }
}