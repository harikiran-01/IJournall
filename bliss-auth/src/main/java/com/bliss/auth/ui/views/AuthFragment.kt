package com.bliss.auth.ui.views

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bliss.auth.R
import com.bliss.auth.databinding.FragmentAuthBinding
import com.bliss.auth.ui.AuthBaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class AuthFragment : AuthBaseFragment<FragmentAuthBinding, Nothing>() {

    @Inject
    lateinit var loginFragment : dagger.Lazy<LoginFragment>
    @Inject
    lateinit var registerFragment : dagger.Lazy<RegisterFragment>

    @StringRes
    private val tabTitles = intArrayOf(R.string.login_tab_text, R.string.register_tab_text)

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private lateinit var viewPager: ViewPager2

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var pagerAdapter: FragmentStateAdapter? = null

    override fun getViewModelClass(): Class<Nothing>? {
        return null
    }

    override fun getViewBinding(): FragmentAuthBinding {
        return FragmentAuthBinding.inflate(layoutInflater)
    }

    override fun setUpViews() {
        super.setUpViews()
        viewPager = binding.pager
        viewPager.offscreenPageLimit = 1
        pagerAdapter = ScreenSlidePagerAdapter(
            childFragmentManager,
            lifecycle,
            loginFragment,
            registerFragment
        )
        viewPager.adapter = pagerAdapter
        binding.lifecycleOwner = this
        val tabLayout = binding.accessTabs
        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int -> tab.setText(tabTitles[position]) }.attach()
    }

    override fun doViewCleanup() {
        super.doViewCleanup()
        viewPager.adapter = null
    }

    class ScreenSlidePagerAdapter constructor(
        fm: FragmentManager,
        lifecycle: Lifecycle,
        private val loginFragment: dagger.Lazy<LoginFragment>,
        private val registerFragment: dagger.Lazy<RegisterFragment>
    ) :
        FragmentStateAdapter(fm, lifecycle) {
        // The number of tabs.
        private val tabCount = 2
        override fun createFragment(position: Int): Fragment {
            return if (position == 0)
                loginFragment.get()
            else registerFragment.get()
        }

        override fun getItemCount(): Int {
            return tabCount
        }
    }
}