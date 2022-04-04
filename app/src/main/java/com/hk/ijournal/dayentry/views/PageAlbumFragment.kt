package com.hk.ijournal.dayentry.views
//
//import android.graphics.Bitmap
//import android.os.Build
//import android.provider.MediaStore
//import androidx.annotation.RequiresApi
//import androidx.fragment.app.activityViewModels
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.bumptech.glide.Glide
//import com.hk.ijournal.common.base.BaseFragment
//import com.hk.ijournal.databinding.FragmentPageAlbumBinding
//import com.hk.ijournal.dayentry.viewmodel.DayEntryViewModel
//import com.hk.ijournal.utils.UriConverter
//import com.hk.ijournal.viewmodels.RelayViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.withContext
//import java.io.ByteArrayOutputStream
//
//
//class PageAlbumFragment : BaseFragment<FragmentPageAlbumBinding, Nothing>() {
//    private val relayViewModel by activityViewModels<RelayViewModel>()
//    private val diaryViewModel: DayEntryViewModel by viewModels(
//        ownerProducer = { requireParentFragment() })
//    private var loadStream: Job? = null
//    private val pageAlbumAdapter by lazy { PageAlbumAdapter(Glide.with(requireContext())) }
//
//    override fun getViewModelClass(): Class<Nothing>? {
//        return null
//    }
//
//    override fun getViewBinding(): FragmentPageAlbumBinding {
//        return FragmentPageAlbumBinding.inflate(layoutInflater)
//    }
//
//    override fun setUpViews() {
//        super.setUpViews()
//        binding.pageAlbumFragment = this
//        binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
//            LinearLayoutManager.HORIZONTAL, false)
//        binding.albumRecyclerView.adapter = pageAlbumAdapter
//        binding.lifecycleOwner = viewLifecycleOwner
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun observeData() {
//        super.observeData()
//        relayViewModel.imageUriList.observe(viewLifecycleOwner) {
//            it?.let {
//                viewLifecycleOwner.lifecycleScope.launchWhenCreated {
//                    diaryViewModel.saveImagesAsAlbum(it)
//                }
//            }
//        }
//
////        diaryViewModel.diaryRepository.currentExternalImgList.observe(viewLifecycleOwner, Observer {
////            loadStream = lifecycleScope.launchWhenCreated {
////                println("persistdeb $it")
////                ensureActive()
////
////                val flowImgStream: Flow<ByteArrayOutputStream> = flow {
////                    it.forEach { externalImg ->
////                        emit(asyncLoadBitmap(externalImg))
////                    }
////                }
////                diaryViewModel.sendStreamFlow(requireContext().filesDir, flowImgStream)
////            }
////        })
//
//        diaryViewModel.selectedDateLive.observe(viewLifecycleOwner) {
//            pageAlbumAdapter.clearAlbum()
//            loadStream?.run {
//                if (isActive)
//                    cancel()
//            }
//        }
//
//        diaryViewModel.albumLive.observe(viewLifecycleOwner
//        ) { imageList ->
//            if (imageList.isNotEmpty()) pageAlbumAdapter.addAlbum(imageList)
//        }
//    }
//
//    override fun doViewCleanup() {
//        binding.albumRecyclerView.adapter = null
//        relayViewModel.resetImagesPickedLiveData()
//        super.doViewCleanup()
//    }
//
//
//
//    private suspend fun asyncLoadBitmap(imageUri: String) = withContext(Dispatchers.IO) {
//        val bitmap = MediaStore.Images.Media.getBitmap(
//            requireContext().contentResolver,
//            UriConverter.stringToUri(imageUri)
//        )
//        val bytes = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
//        bitmap.recycle()
//        bytes
//    }
//
//    fun onAddImageFromDevice() {
//        relayViewModel.imagePickerClicked.set(true)
//    }
//}