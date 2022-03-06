package com.hk.ijournal.dayentry.views

import android.app.DatePickerDialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.hk.ijournal.R
import com.hk.ijournal.common.base.BaseFragment
import com.hk.ijournal.databinding.FragmentDayEntryBinding
import com.hk.ijournal.dayentry.SmileyRatingFragment
import com.hk.ijournal.dayentry.viewmodel.DayEntryViewModel
import com.hk.ijournal.repository.data.source.local.IJDatabase
import com.hk.ijournal.repository.models.ContentType
import com.hk.ijournal.utils.SessionAuthManager
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
class DayEntryFragment : BaseFragment<FragmentDayEntryBinding, Nothing>(), View.OnClickListener, View.OnLongClickListener, DatePickerDialog.OnDateSetListener {
    private val safeArgs : DayEntryFragmentArgs by navArgs()
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private val dayEntryViewModel : DayEntryViewModel by viewModels()
    private lateinit var datePickerDialog: DatePickerDialog

    @RequiresApi(Build.VERSION_CODES.O)
    private val textWatcher = DebouncingEditTextWatcher(lifecycleScope) { typedContent, stoppedTyping ->
        dayEntryViewModel.postContent(typedContent, stoppedTyping)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getViewModelClass(): Class<Nothing>? {
        return null
    }

    override fun getViewBinding(): FragmentDayEntryBinding {
        return FragmentDayEntryBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpViews() {
        super.setUpViews()
        datePickerDialog = DatePickerDialog(requireContext(), this, Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH] + 1,
            Calendar.getInstance()[Calendar.DAY_OF_MONTH])
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.setOnDateSetListener(this)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dayEntryViewModel = dayEntryViewModel
        binding.dayEntryFragment = this
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpListeners() {
        super.setUpListeners()
        binding.smileyRatingButton.setOnClickListener(this)
        binding.smileyRatingButton.setOnLongClickListener(this)
        binding.diaryContent.addTextChangedListener(textWatcher)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeData() {
        super.observeData()
        dayEntryViewModel.pageContentLive.observe(viewLifecycleOwner, Observer {
            DebouncingEditTextWatcher.typedText = it.contentType == ContentType.TYPED
            if (it.contentType == ContentType.LOADED)
                binding.diaryContent.setText(it.text)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doViewCleanup() {
        dayEntryViewModel.resetSavedStatus()
        DebouncingEditTextWatcher.typedText = false
        binding.diaryContent.removeTextChangedListener(textWatcher)
        super.doViewCleanup()
    }

    private fun toggleRatingSelectorDialog() {
        val ft = childFragmentManager.beginTransaction()
        val existingFrag = childFragmentManager.findFragmentByTag(SmileyRatingFragment.FRAG_NAME)
        existingFrag?.let {
            ft.remove(it).commit()
        }
                ?: ft.add(R.id.rating_holder,
                    SmileyRatingFragment.newInstance(),
                    SmileyRatingFragment.FRAG_NAME
                ).commit()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.smiley_rating_button -> {
                SessionAuthManager.logoutUser()
                relayViewModel.onSessionEnd.set(true)
                //toggleRatingSelectorDialog()
            }
        }
    }

    fun showDatePicker() {
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dayEntryViewModel.navigateToSelectedPage(LocalDate.of(year, month+1, dayOfMonth))
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

    companion object {
        var typedText: Boolean = false
    }



    private var editTextJob: Job? = null


    override fun afterTextChanged(s: Editable?) {
    }

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


