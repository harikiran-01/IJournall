package com.hk.ijournal.feed.views

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hk.ijournal.common.base.BaseFragment
import com.hk.ijournal.databinding.FragmentFeedBinding
import com.hk.ijournal.decoration.VerticalItemDecoration
import com.hk.ijournal.feed.adapters.FeedAdapter
import com.hk.ijournal.feed.viewmodel.FeedViewModel
import com.hk.ijournal.views.LaunchActivity
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
        viewModel.getAllPages()
    }

    override fun setUpListeners() {
        super.setUpListeners()
        binding.addEntryBtn.setOnClickListener {
            (requireActivity() as LaunchActivity).supportActionBar?.hide()
            findNavController().navigate(FeedFragmentDirections.feedToDayEntry(safeArgs.diaryUser))
        }

        binding.calendarBtn.setOnClickListener {
            findNavController().navigate(FeedFragmentDirections.feedToCalendar(safeArgs.diaryUser))
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.allPages.observe(viewLifecycleOwner){
            feedAdapter.setItems(it)
        }
    }


    override fun doViewCleanup() {
        feedAdapter.clear()
        binding.miniPageRv.adapter = null
        super.doViewCleanup()
    }
}