package com.hk.ijournal.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hk.ijournal.R;
import com.hk.ijournal.databinding.FragmentAccessBinding;

public class AccessFragment extends Fragment {
    @StringRes
    private final int[] TAB_TITLES = new int[]{R.string.login_tab_text, R.string.register_tab_text};
    FragmentAccessBinding accessBinding;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    public AccessFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycle", "accessF onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        accessBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_access, container, false);
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = accessBinding.pager;
        pagerAdapter = new ScreenSlidePagerAdapter(requireActivity());
        viewPager.setAdapter(pagerAdapter);
        accessBinding.setLifecycleOwner(this);

        TabLayout tabLayout = accessBinding.accessTabs;
        TabLayout dotTabs = accessBinding.tabsDots;

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(TAB_TITLES[position])
        ).attach();

        new TabLayoutMediator(dotTabs, viewPager,
                (tab, position) -> {
                }
        ).attach();

        return accessBinding.getRoot();
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        // The number of tabs.
        private final int NUM_TABS = 2;

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            Log.d("lifecycle", "viewpager createFragment");
            if (position == 0)
                return new LoginFragment();
            else
                return new RegisterFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }
}