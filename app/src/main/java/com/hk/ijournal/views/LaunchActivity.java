package com.hk.ijournal.views;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.hk.ijournal.R;
import com.hk.ijournal.databinding.ActivityLaunchBinding;
import com.hk.ijournal.viewmodels.AccessViewModel;

public class LaunchActivity extends AppCompatActivity {
    ActivityLaunchBinding launchBinding;

    public static AccessViewModel obtainViewModel(FragmentActivity activity) {
        return new ViewModelProvider(activity, new ViewModelProvider.AndroidViewModelFactory(activity.getApplication())).get(AccessViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("lifecycle", "launchA onCreate");
        super.onCreate(savedInstanceState);
        launchBinding = DataBindingUtil.setContentView(this, R.layout.activity_launch);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.view_fragment, new AccessFragment());
        ft.commit();
    }

//    @Override
//    public void onBackPressed() {
//        if (viewPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//        }
//    }

    public void navigateTo(Fragment newFragment, boolean b) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_fragment, newFragment);
        ft.commit();
        //ft.addToBackStack(null);
    }
}
