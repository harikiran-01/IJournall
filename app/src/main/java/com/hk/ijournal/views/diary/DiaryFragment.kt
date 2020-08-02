package com.hk.ijournal.views.diary

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentDiaryBinding
import com.hk.ijournal.viewmodels.DiaryViewModel
import com.hk.ijournal.viewmodels.DiaryViewModelFactory
import com.hk.ijournal.viewmodels.HomeViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class DiaryFragment : Fragment(), View.OnClickListener, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @RequiresApi(Build.VERSION_CODES.O)
    private val textWatcher = DebouncingEditTextWatcher(this) { typedContent, stoppedTyping ->
        diaryViewModel.postContent(typedContent, stoppedTyping)
    }

    private lateinit var diaryViewModel: DiaryViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fragmentDiaryBinding: FragmentDiaryBinding

    companion object DiaryFragFactory {
        fun newInstance(userId: Long): DiaryFragment {
            val diaryFragment = DiaryFragment()
            val args = Bundle()
            args.putLong("uid", userId)
            diaryFragment.arguments = args
            return diaryFragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("lifecycle", "diaryF onCreateView")
        fragmentDiaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)
        fragmentDiaryBinding.smileyRatingButton.setOnClickListener(this)
        fragmentDiaryBinding.prevDateButton.setOnClickListener(this)
        fragmentDiaryBinding.nextDateButton.setOnClickListener(this)
        fragmentDiaryBinding.diaryContent.addTextChangedListener(textWatcher)
        return fragmentDiaryBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroyView() {
        Log.d("lifecycle", "diaryF onDView")
        fragmentDiaryBinding.diaryContent.removeTextChangedListener(textWatcher)
        fragmentDiaryBinding.unbind()
        super.onDestroyView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        diaryViewModel = ViewModelProvider(this,
                DiaryViewModelFactory(requireActivity().application, requireArguments().getLong("uid"))).get(DiaryViewModel::class.java)
        fragmentDiaryBinding.diaryViewModel = diaryViewModel
        fragmentDiaryBinding.lifecycleOwner = viewLifecycleOwner
        observeViewModel()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        diaryViewModel.pageContent.observe(viewLifecycleOwner, Observer {
            textWatcher.typedText = false
            fragmentDiaryBinding.diaryContent.setText(it)
            textWatcher.typedText = true
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle", "diaryF onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifecycle", "diaryF onDestroyed")
    }

    private fun toggleRatingSelectorDialog() {
        val ft = childFragmentManager.beginTransaction()
        val existingFrag = childFragmentManager.findFragmentByTag(SmileyRatingFragment.FRAG_NAME)
        existingFrag?.let {
            ft.remove(it).commit()
        }
                ?: ft.add(R.id.rating_holder, SmileyRatingFragment.newInstance(), SmileyRatingFragment.FRAG_NAME).commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.smiley_rating_button -> {
                toggleRatingSelectorDialog()
                //RoomExplorer.show(requireActivity(), IJDatabase::class.java, "Journals.db")
            }

            R.id.prevDateButton -> {
                //parentFragmentManager.beginTransaction().detach(this).attach(this).commitNowAllowingStateLoss()
                diaryViewModel.navigateToPrevPage()
            }

            R.id.nextDateButton -> {
                //parentFragmentManager.beginTransaction().detach(this).attach(this).commitNowAllowingStateLoss()
                diaryViewModel.navigateToNextPage()
            }
        }
    }
}

internal class DebouncingEditTextWatcher(private val coroutineScope: CoroutineScope,
                                         private val onDebouncingEditTextChange: (String, Boolean) -> Unit
) : TextWatcher {
    private val debouncePeriod: Long = 500
    var typedText: Boolean = true

    private var editTextJob: Job? = null


    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val content = s.toString()
        editTextJob?.cancel()
        if (content.isNotBlank() && typedText) {
            editTextJob = coroutineScope.launch {
                content.let {
                    onDebouncingEditTextChange(it, false)
                    delay(debouncePeriod)
                    onDebouncingEditTextChange(it, true)
                }
            }
        }
    }
}

