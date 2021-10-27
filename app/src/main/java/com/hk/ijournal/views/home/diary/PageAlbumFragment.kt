package com.hk.ijournal.views.home.diary

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hk.ijournal.R
import com.hk.ijournal.adapters.PageAlbumAdapter
import com.hk.ijournal.databinding.FragmentPageAlbumBinding
import com.hk.ijournal.utils.UriConverter
import com.hk.ijournal.viewmodels.DiaryViewModel
import com.hk.ijournal.viewmodels.DiaryViewModelFactory
import com.hk.ijournal.viewmodels.RelayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class PageAlbumFragment : Fragment(), View.OnClickListener, LifecycleObserver {
    private lateinit var pageAlbumBinding: FragmentPageAlbumBinding
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private val diaryViewModel: DiaryViewModel by viewModels (
        ownerProducer = { requireParentFragment() })
    private var loadStream: Job? = null
    private val pageAlbumAdapter by lazy { PageAlbumAdapter(Glide.with(requireContext())) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        pageAlbumBinding = FragmentPageAlbumBinding.inflate(inflater, container, false)
        pageAlbumBinding.addImageButton.setOnClickListener(this)
        return pageAlbumBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageAlbumBinding.albumRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL, false)
        pageAlbumBinding.albumRecyclerView.adapter = pageAlbumAdapter
        pageAlbumBinding.lifecycleOwner = viewLifecycleOwner
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeVM()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeVM() {
        relayViewModel.imageUriCategory.observe(viewLifecycleOwner, {
            requireParentFragment().requireParentFragment().arguments?.getStringArrayList("imageuridata")?.let {
                lifecycleScope.launchWhenCreated {
                    diaryViewModel.saveImagesData(it)

                }
            }
        })

        diaryViewModel.diaryRepository.currentExternalImgList.observe(viewLifecycleOwner, {
            loadStream = lifecycleScope.launchWhenCreated {
                println("persistdeb $it")
                ensureActive()

                val flowImgStream: Flow<ByteArrayOutputStream> = flow {
                    it.forEach { externalImg ->
                        emit(asyncLoadBitmap(externalImg))
                    }
                }
                diaryViewModel.sendStreamFlow(requireContext().filesDir, flowImgStream)
            }
        })

        diaryViewModel.selectedDateLive.observe(viewLifecycleOwner, {
            pageAlbumAdapter.clearAlbum()
            loadStream?.run {
                if (isActive)
                    cancel()
            }
        })

        diaryViewModel.diaryRepository.albumLive.observe(viewLifecycleOwner, { imageList ->
            println("albdeb $imageList")
            if (imageList.isNotEmpty()) pageAlbumAdapter.addAlbum(imageList)
        }
        )
    }

    private suspend fun asyncLoadBitmap(imageUri: String) = withContext(Dispatchers.IO) {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, UriConverter.stringToUri(imageUri))
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
        bitmap.recycle()
        bytes
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.addImageButton) {
            relayViewModel.imagePickerClicked.set(true)
        }
    }

}