package com.hk.ijournal.feed.views

import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hk.ijournal.common.base.BaseFragment
import com.hk.ijournal.common.decoration.VerticalItemDecoration
import com.hk.ijournal.databinding.FragmentFeedBinding
import com.hk.ijournal.feed.adapters.FeedAdapter
import com.hk.ijournal.feed.adapters.viewbinders.LandingFeedBinder
import com.hk.ijournal.feed.viewmodel.FeedViewModel
import com.hk.ijournal.views.LaunchActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding, FeedViewModel>() {
    private val safeArgs: FeedFragmentArgs by navArgs()

    lateinit var feedAdapter: FeedAdapter

    override fun getViewModelClass(): Class<FeedViewModel> {
        return FeedViewModel::class.java
    }

    override fun getViewBinding(): FragmentFeedBinding {
        return FragmentFeedBinding.inflate(layoutInflater)
    }

    override fun setUpViews() {
        super.setUpViews()
        initAdapter()
        viewModel.getAllPages()
    }

    private fun initAdapter() {
        feedAdapter = FeedAdapter(LandingFeedBinder {
            findNavController().navigate(FeedFragmentDirections.feedToDayEntryPreview(safeArgs.diaryUser, it))
        })
        with(binding) {
            miniPageRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(VerticalItemDecoration(30))
                adapter = feedAdapter
            }
        }
    }

    override fun setUpListeners() {
        super.setUpListeners()
        binding.addEntryBtn.setOnClickListener {
            (requireActivity() as LaunchActivity).supportActionBar?.hide()
            Log.d("DEBDEB", safeArgs.diaryUser.toString())
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