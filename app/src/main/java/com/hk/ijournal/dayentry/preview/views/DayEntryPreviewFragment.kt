package com.hk.ijournal.dayentry.preview.views

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hk.ijournal.common.base.BaseAdapterViewType
import com.hk.ijournal.common.base.BaseFragment
import com.hk.ijournal.common.decoration.VerticalItemDecoration
import com.hk.ijournal.common.toPx
import com.hk.ijournal.databinding.FragDayEntryPreviewBinding
import com.hk.ijournal.dayentry.preview.adapters.EntryContentPreviewAdapter
import com.hk.ijournal.dayentry.preview.viewmodel.DayEntryPreviewVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DayEntryPreviewFragment : BaseFragment<FragDayEntryPreviewBinding, Nothing>() {
    private val safeArgs : DayEntryPreviewFragmentArgs by navArgs()
    private val dayEntryPreviewVM : DayEntryPreviewVM by viewModels()

    @Inject
    lateinit var entryContentPreviewAdapter: EntryContentPreviewAdapter

    private val textWatcher = DebouncingEditTextWatcher(lifecycleScope) { typedContent, stoppedTyping ->
        // no ops
    }

    override fun getViewModelClass(): Class<Nothing>? {
        return null
    }

    override fun getViewBinding(): FragDayEntryPreviewBinding {
        return FragDayEntryPreviewBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpViews() {
        super.setUpViews()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            dayEntryPreviewViewModel = dayEntryPreviewVM
            dayEntryPreviewFragment = this@DayEntryPreviewFragment
        }
        initAdapter()
        dayEntryPreviewVM.loadPage(safeArgs.pageId)
    }

    private fun initAdapter() {
        with(binding) {
            contentRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(VerticalItemDecoration(25.toPx.toInt()))
                adapter = entryContentPreviewAdapter
            }
        }
    }

    override fun setUpListeners() {
        super.setUpListeners()
        with(binding) {
            editBtn.setOnClickListener {
                findNavController().navigate(DayEntryPreviewFragmentDirections.dayentryPreviewToDayentryEdit(safeArgs.diaryUser, safeArgs.pageId))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeData() {
        dayEntryPreviewVM.currentPage.observe(viewLifecycleOwner) { page ->
            binding.title.setText(page.title)
            val contentListForAdapter = page.contentList.map { it.data as BaseAdapterViewType }
            entryContentPreviewAdapter.setItems(contentListForAdapter)
        }
    }

    override fun doViewCleanup() {
        super.doViewCleanup()
        binding.contentRv.adapter = null
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


