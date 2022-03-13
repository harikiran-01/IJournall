package com.hk.ijournal.dayentry.views

import android.app.DatePickerDialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hk.ijournal.common.base.BaseFragment
import com.hk.ijournal.databinding.FragmentDayEntryBinding
import com.hk.ijournal.dayentry.adapters.EntryContentAdapter
import com.hk.ijournal.dayentry.models.TextModel
import com.hk.ijournal.dayentry.viewmodel.DayEntryViewModel
import com.hk.ijournal.decoration.VerticalItemDecoration
import com.hk.ijournal.repository.data.source.local.entities.ImageContent
import com.hk.ijournal.viewmodels.RelayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DayEntryFragment : BaseFragment<FragmentDayEntryBinding, Nothing>(), DatePickerDialog.OnDateSetListener {
    private val safeArgs : DayEntryFragmentArgs by navArgs()
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private val dayEntryViewModel : DayEntryViewModel by viewModels()
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
        setupDatePicker()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            dayEntryViewModel = dayEntryViewModel
            dayEntryFragment = this@DayEntryFragment
        }
        initAdapter()
    }

    private fun initAdapter() {
        with(binding) {
            contentRv.apply {
                layoutManager = object : LinearLayoutManager(requireContext()) {
                    override fun canScrollVertically(): Boolean { return false }
                }
                addItemDecoration(VerticalItemDecoration(30))
                adapter = entryContentAdapter
            }
        }
        entryContentAdapter.addItem(TextModel("Hey", "#A02B55"))
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
                        val imageContentList = imageUris.map { ImageContent(it, "") }
                        entryContentAdapter.addItems(imageContentList)
                        //dayEntryViewModel.saveImagesAsAlbum(it)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeData() {
        super.observeData()

        dayEntryViewModel.pageTitleLive.observe(viewLifecycleOwner, Observer {
            binding.title.setText(it)
        })

//        dayEntryViewModel.pageContentLive.observe(viewLifecycleOwner, Observer {
//            if (it.contentType == ContentType.LOADED)
//                binding.content.setText(it.text)
//        })
    }

    fun showDatePicker() {
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dayEntryViewModel.navigateToSelectedPage(LocalDate.of(year, month+1, dayOfMonth))
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


