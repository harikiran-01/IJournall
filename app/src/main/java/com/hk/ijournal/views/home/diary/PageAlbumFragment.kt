package com.hk.ijournal.views.home.diary

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hk.ijournal.adapters.PageAlbumAdapter
import com.hk.ijournal.databinding.FragmentPageAlbumBinding
import com.hk.ijournal.utils.UriConverter
import com.hk.ijournal.viewmodels.DiaryViewModel
import com.hk.ijournal.viewmodels.RelayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class PageAlbumFragment : Fragment() {
    private lateinit var pageAlbumBinding: FragmentPageAlbumBinding
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private val diaryViewModel: DiaryViewModel by viewModels(
        ownerProducer = { requireParentFragment() })
    private var loadStream: Job? = null
    private val pageAlbumAdapter by lazy { PageAlbumAdapter(Glide.with(requireContext())) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pageAlbumBinding = FragmentPageAlbumBinding.inflate(inflater, container, false)
        pageAlbumBinding.pageAlbumFragment = this
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
        relayViewModel.imageUriList.observe(viewLifecycleOwner, Observer {
            requireParentFragment().requireParentFragment().arguments?.getStringArrayList("imageuridata")
                ?.let {
                    lifecycleScope.launchWhenCreated {
                        diaryViewModel.saveImagesAsAlbum(it)
                    }
                }
        })

//        diaryViewModel.diaryRepository.currentExternalImgList.observe(viewLifecycleOwner, Observer {
//            loadStream = lifecycleScope.launchWhenCreated {
//                println("persistdeb $it")
//                ensureActive()
//
//                val flowImgStream: Flow<ByteArrayOutputStream> = flow {
//                    it.forEach { externalImg ->
//                        emit(asyncLoadBitmap(externalImg))
//                    }
//                }
//                diaryViewModel.sendStreamFlow(requireContext().filesDir, flowImgStream)
//            }
//        })

        diaryViewModel.selectedDateLive.observe(viewLifecycleOwner) {
            pageAlbumAdapter.clearAlbum()
            loadStream?.run {
                if (isActive)
                    cancel()
            }
        }

        diaryViewModel.albumLive.observe(viewLifecycleOwner
        ) { imageList ->
            println("albdeb $imageList")
            if (imageList.isNotEmpty()) pageAlbumAdapter.addAlbum(imageList)
        }
    }

    private suspend fun asyncLoadBitmap(imageUri: String) = withContext(Dispatchers.IO) {
        val bitmap = MediaStore.Images.Media.getBitmap(
            requireContext().contentResolver,
            UriConverter.stringToUri(imageUri)
        )
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
        bitmap.recycle()
        bytes
    }

    fun onAddImageFromDevice() {
        relayViewModel.imagePickerClicked.set(true)
    }
}