package com.hk.ijournal.views.home.diary

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hk.ijournal.R
import com.hk.ijournal.adapters.PageAlbumAdapter
import com.hk.ijournal.databinding.FragmentPageAlbumBinding
import com.hk.ijournal.utils.UriConverter
import com.hk.ijournal.viewmodels.DiaryViewModel
import com.hk.ijournal.viewmodels.RelayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class PageAlbumFragment : Fragment(), View.OnClickListener {
    private lateinit var pageAlbumBinding: FragmentPageAlbumBinding
    private val relayViewModel by activityViewModels<RelayViewModel>()
    private lateinit var diaryViewModel: DiaryViewModel
    private var loadStream: Job? = null
    private val pageAlbumAdapter by lazy { PageAlbumAdapter(Glide.with(requireContext())) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        pageAlbumBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_page_album, container, false)
        pageAlbumBinding.addImageButton.setOnClickListener(this)
        return pageAlbumBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageAlbumBinding.albumRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        diaryViewModel = ViewModelProvider(requireParentFragment()).get(DiaryViewModel::class.java)
        pageAlbumBinding.albumRecyclerView.adapter = pageAlbumAdapter
        observeVM()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeVM() {
        relayViewModel.imageUriCategory.observe(viewLifecycleOwner, Observer {
            requireParentFragment().requireParentFragment().arguments?.getStringArrayList("imageuridata")?.let {
                lifecycleScope.launchWhenCreated {
                    diaryViewModel.saveImagesData(it)

                }
            }
        })

        diaryViewModel.currentExternalImgList.observe(viewLifecycleOwner, Observer {
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

        diaryViewModel.selectedDateLive.observe(viewLifecycleOwner, Observer {
            pageAlbumAdapter.clearAlbum()
            loadStream?.run {
                if (isActive)
                    cancel()
            }
        })

        diaryViewModel.dayAlbumLive.observe(viewLifecycleOwner, Observer { imageList ->
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