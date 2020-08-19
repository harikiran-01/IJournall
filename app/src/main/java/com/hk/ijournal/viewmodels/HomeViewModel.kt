package com.hk.ijournal.viewmodels

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var lastActiveFragTag: String = ""

    fun setLastActiveFragmentTag(tag: String) {
        lastActiveFragTag = tag
    }
}