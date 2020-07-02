package com.hk.ijournal.views.ui.diary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.hk.ijournal.R;
import com.hk.ijournal.databinding.FragmentDiaryBinding;


public class DiaryFragment extends Fragment implements View.OnClickListener {

    private DiaryViewModel diaryViewModel;
    private FragmentDiaryBinding fragmentDiaryBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diaryViewModel =
                ViewModelProviders.of(this).get(DiaryViewModel.class);
        fragmentDiaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false);

        fragmentDiaryBinding.smileyRatingButton.setOnClickListener(this);
        return fragmentDiaryBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycle", "diaryF onCreate");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.smiley_rating_button) {
            SmileyRatingFragment smileyRatingFragment = new SmileyRatingFragment(v);
            smileyRatingFragment.show(getChildFragmentManager(), "smiley");
        }

    }
}