package com.hk.ijournal.feed.views

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hk.ijournal.common.base.BaseFragment
import com.hk.ijournal.databinding.FragmentFeedBinding
import com.hk.ijournal.decoration.VerticalItemDecoration
import com.hk.ijournal.feed.adapters.FeedAdapter
import com.hk.ijournal.feed.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>() {
    private val safeArgs: FeedFragmentArgs by navArgs()

    @Inject
    lateinit var feedAdapter: FeedAdapter

    override fun getViewModelClass(): Class<FeedViewModel> {
        return FeedViewModel::class.java
    }

    override fun getViewBinding(): com.hk.ijournal.databinding.FragmentFeedBinding {
        return FragmentFeedBinding.inflate(layoutInflater)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.miniPageRv.layoutManager = LinearLayoutManager(requireContext())
        binding.miniPageRv.addItemDecoration(
            VerticalItemDecoration(
                30
            )
        )
        binding.miniPageRv.adapter = feedAdapter
    }

    override fun setUpListeners() {
        super.setUpListeners()
        binding.addEntryBtn.setOnClickListener {
            findNavController().navigate(FeedFragmentDirections.feedToDayEntry(safeArgs.diaryUser))
        }

        binding.calendarBtn.setOnClickListener {
            findNavController().navigate(FeedFragmentDirections.feedToCalendar(safeArgs.diaryUser))
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.allPages.observe(this){
            feedAdapter.addItems(it)
        }
    }


    override fun doViewCleanup() {
        binding.miniPageRv.adapter = null
        super.doViewCleanup()
    }
}