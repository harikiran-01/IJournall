package com.hk.ijournal.features.feed.views

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hk.ijournal.common.decoration.VerticalItemDecoration
import com.hk.ijournal.databinding.FragmentFeedBinding
import com.hk.ijournal.features.feed.adapters.FeedAdapter
import com.hk.ijournal.features.feed.adapters.viewbinders.LandingFeedBinder
import com.hk.ijournal.features.feed.viewmodel.FeedViewModel
import com.hk.ijournal.features.search.views.SearchableActivity
import com.hk.ijournal.viewmodels.RelayViewModel
import com.hk.ijournal.views.RootActivity
import dagger.hilt.android.AndroidEntryPoint
import omni.platform.android.components.android.BaseBindingFragment


@AndroidEntryPoint
class FeedFragment : BaseBindingFragment<FragmentFeedBinding, FeedViewModel>() {
    companion object {
        const val TAG = "FeedFragment"
    }

    private val relayViewModel by activityViewModels<RelayViewModel>()
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
            findNavController().navigate(FeedFragmentDirections.feedToDayEntryPreview(it))
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
        with(binding) {
            addEntryBtn.setOnClickListener {
                (requireActivity() as RootActivity).supportActionBar?.hide()
//                findNavController().navigate(FeedFragmentDirections.feedToDayEntry(safeArgs.diaryUser))
            }

            calendarBtn.setOnClickListener {
//                findNavController().navigate(FeedFragmentDirections.feedToCalendar(safeArgs.diaryUser))
            }

            fullSearch.setOnClickListener {
                startActivity(Intent(requireActivity(), SearchableActivity::class.java).apply {
//                    putExtra("uid", safeArgs.diaryUser.uid)
                })
            }

            fullSearch.setOnLongClickListener {
                relayViewModel.onSessionEnd.set(true)
                return@setOnLongClickListener true
            }
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