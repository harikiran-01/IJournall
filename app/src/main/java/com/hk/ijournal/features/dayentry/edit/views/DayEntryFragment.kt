package com.hk.ijournal.features.dayentry.edit.views

import android.app.DatePickerDialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hk.ijournal.common.decoration.VerticalItemDecoration
import com.hk.ijournal.databinding.FragmentDayEntryBinding
import com.hk.ijournal.features.dayentry.edit.adapters.EntryContentAdapter
import com.hk.ijournal.features.dayentry.edit.viewmodel.DayEntryVM
import com.hk.ijournal.features.dayentry.models.content.MediaContent
import com.hk.ijournal.features.dayentry.preview.views.DayEntryPreviewFragmentArgs
import com.hk.ijournal.features.dayentry.preview.views.DebouncingEditTextWatcher
import com.hk.ijournal.viewmodels.RelayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import omni.platform.android.components.android.BaseBindingFragment
import omni.platform.android.components.android.adapters.BaseAdapterViewType
import omni.platform.android.extensions.toPx
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class DayEntryFragment : BaseBindingFragment<FragmentDayEntryBinding, Nothing>(), DatePickerDialog.OnDateSetListener {
    private val safeArgs : DayEntryPreviewFragmentArgs by navArgs()
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private val dayEntryVM : DayEntryVM by viewModels()
    private lateinit var datePickerDialog: DatePickerDialog

    @Inject
    lateinit var entryContentAdapter: EntryContentAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    private val textWatcher = DebouncingEditTextWatcher(lifecycleScope) { typedContent, stoppedTyping ->
        // no ops
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
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            dayEntryViewModel = dayEntryVM
            dayEntryFragment = this@DayEntryFragment
        }
        initAdapter()
        setupDatePicker()
    }

    private fun initAdapter() {
        with(binding) {
            contentRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(VerticalItemDecoration(25.toPx))
                adapter = entryContentAdapter
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDatePicker() {
        datePickerDialog = DatePickerDialog(requireContext(), this, Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH] + 1,
            Calendar.getInstance()[Calendar.DAY_OF_MONTH])
        datePickerDialog.apply {
            datePicker.maxDate = Calendar.getInstance().timeInMillis
            setOnDateSetListener(this@DayEntryFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpListeners() {
        super.setUpListeners()
        with(binding) {

            saveBtn.setOnClickListener {
                dayEntryViewModel?.savePage(binding.title.text.toString(), entryContentAdapter.dataList)
            }

            addImageBtn.setOnClickListener {
                relayViewModel.imagePickerClicked.set(true)
            }

            relayViewModel.imageUriList.observe(viewLifecycleOwner) { imageUris ->
                imageUris?.let {
                    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                        val imageContentList = imageUris.map { MediaContent(it.first, it.second,"") }
                        entryContentAdapter.addItems(imageContentList)
                        scrollView.isSmoothScrollingEnabled = true
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeData() {
        super.observeData()
        dayEntryVM.currentPage.observe(viewLifecycleOwner) { page ->
            binding.title.setText(page.title)
            val contentListForAdapter = page.contentList.map { it.data as BaseAdapterViewType }
            entryContentAdapter.setItems(contentListForAdapter)
        }
    }

    fun showDatePicker() {
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dayEntryVM.navigateToSelectedPage(LocalDate.of(year, month+1, dayOfMonth))
    }

    override fun doViewCleanup() {
        super.doViewCleanup()
        binding.contentRv.adapter = null
    }

    companion object {
        const val TAG = "DayEntryFragment"
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


