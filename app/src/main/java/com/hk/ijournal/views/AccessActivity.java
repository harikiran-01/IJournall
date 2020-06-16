package com.hk.ijournal.views;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.hk.ijournal.R;
import com.hk.ijournal.databinding.ActivityAccessBinding;
import com.hk.ijournal.viewmodels.AccessViewModel;

public class AccessActivity extends FragmentActivity {
    AccessViewModel accessViewModel;
    ActivityAccessBinding accessBinding;
    // The number of pages (wizard steps) to show in this demo.
    private static final int NUM_PAGES = 2;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessBinding = DataBindingUtil.setContentView(this, R.layout.activity_access);
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        accessViewModel = ViewModelProviders.of(this).get(AccessViewModel.class);
        accessBinding.setLifecycleOwner(this);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            if(position==0)
                return new LoginFragment();
            else
                return new RegisterFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
