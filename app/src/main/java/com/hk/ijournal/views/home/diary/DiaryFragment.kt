package com.hk.ijournal.views.home.diary

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentDiaryBinding
import com.hk.ijournal.repository.data.source.local.IJDatabase
import com.hk.ijournal.repository.data.source.local.entities.DiaryUser
import com.hk.ijournal.repository.models.ContentType
import com.hk.ijournal.utils.SessionAuthManager
import com.hk.ijournal.viewmodels.DiaryViewModel
import com.hk.ijournal.viewmodels.RelayViewModel
import com.wajahatkarim3.roomexplorer.RoomExplorer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@AndroidEntryPoint
class DiaryFragment : Fragment(), View.OnClickListener, View.OnLongClickListener, DatePickerDialog.OnDateSetListener {
    private val safeArgs : DiaryFragmentArgs by navArgs()
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private val diaryViewModel : DiaryViewModel by viewModels()
    private lateinit var datePickerDialog: DatePickerDialog

    @RequiresApi(Build.VERSION_CODES.O)
    private val textWatcher = DebouncingEditTextWatcher(lifecycleScope) { typedContent, stoppedTyping ->
        diaryViewModel.postContent(typedContent, stoppedTyping)
    }

    private lateinit var fragmentDiaryBinding: FragmentDiaryBinding

    companion object DiaryFragFactory {
        fun newInstance(diaryUser: DiaryUser): DiaryFragment {
            val diaryFragment = DiaryFragment()
            val args = Bundle()
            args.putSerializable("diaryUser", diaryUser)
            diaryFragment.arguments = args
            return diaryFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("lifecycled diaryF onCreate")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        println("lifecycled diaryF onCreateView")
        fragmentDiaryBinding = FragmentDiaryBinding.inflate(inflater, container, false)
        fragmentDiaryBinding.smileyRatingButton.setOnClickListener(this)
        fragmentDiaryBinding.smileyRatingButton.setOnLongClickListener(this)
        fragmentDiaryBinding.diaryContent.addTextChangedListener(textWatcher)
        return fragmentDiaryBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePickerDialog = DatePickerDialog(requireContext(), this, Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH] + 1,
                Calendar.getInstance()[Calendar.DAY_OF_MONTH])
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.setOnDateSetListener(this)
        fragmentDiaryBinding.lifecycleOwner = viewLifecycleOwner
        fragmentDiaryBinding.diaryViewModel = diaryViewModel
        fragmentDiaryBinding.diaryFragment = this
        observeViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        diaryViewModel.pageContentLive.observe(viewLifecycleOwner, Observer {
            textWatcher.typedText = it.contentType == ContentType.TYPED
            if (it.contentType == ContentType.LOADED)
                fragmentDiaryBinding.diaryContent.setText(it.text)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroyView() {
        println("lifecycled diaryF onDView")
        fragmentDiaryBinding.diaryContent.removeTextChangedListener(textWatcher)
        fragmentDiaryBinding.unbind()
        super.onDestroyView()
    }

    private fun toggleRatingSelectorDialog() {
        val ft = childFragmentManager.beginTransaction()
        val existingFrag = childFragmentManager.findFragmentByTag(SmileyRatingFragment.FRAG_NAME)
        existingFrag?.let {
            ft.remove(it).commit()
        }
                ?: ft.add(R.id.rating_holder, SmileyRatingFragment.newInstance(), SmileyRatingFragment.FRAG_NAME).commit()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.smiley_rating_button -> {
                SessionAuthManager.logoutUser()
                relayViewModel.onSessionEnd.set(true)
                relayViewModel.onSessionEnd.set(false)
                //toggleRatingSelectorDialog()
            }
        }
    }

    fun showDatePicker() {
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        diaryViewModel.navigateToSelectedPage(LocalDate.of(year, month+1, dayOfMonth))
    }

    override fun onDestroy() {
        super.onDestroy()
        println("lifecycled diaryF onDestroy")
    }

    override fun onLongClick(v: View): Boolean {
        when (v.id) {
            R.id.smiley_rating_button -> {
                RoomExplorer.show(requireActivity(), IJDatabase::class.java, "Journals.db")
            }
        }
        return true
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
        if (!typedText)
            typedText = true
    }
}


