package com.hk.ijournal.views.ui.diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentDiaryBinding
import com.hk.ijournal.viewmodels.DiaryViewModel

class DiaryFragment : Fragment(), View.OnClickListener {

    private lateinit var diaryViewModel: DiaryViewModel
    private lateinit var fragmentDiaryBinding: FragmentDiaryBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        diaryViewModel = ViewModelProviders.of(this).get(DiaryViewModel::class.java)
        fragmentDiaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)
        fragmentDiaryBinding.smileyRatingButton.setOnClickListener(this)
        return fragmentDiaryBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle", "diaryF onCreate")
    }

    override fun onClick(v: View) {
        if (v.id == R.id.smiley_rating_button) {
            val smileyRatingFragment = SmileyRatingFragment(v)
            smileyRatingFragment.show(childFragmentManager, "smiley")
        }
    }
}