package com.hk.ijournal.views.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hk.ijournal.R

class PageAlbumFragment private constructor() : Fragment() {
    private var userId: String? = null
    private var pageId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString(USERID)
        pageId = arguments?.getString(PAGEID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_album, container, false)
    }

    companion object {
        private const val USERID = "uid"
        private const val PAGEID = "pid"

        // TODO: Rename and change types and number of parameters
        fun newInstance(userId: String?, pageId: String?): PageAlbumFragment {
            val fragment = PageAlbumFragment()
            val args = Bundle()
            args.putString(USERID, userId)
            args.putString(PAGEID, pageId)
            fragment.arguments = args
            return fragment
        }
    }
}