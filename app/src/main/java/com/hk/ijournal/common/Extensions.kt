package com.hk.ijournal.common

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyListChanged() {
    this.value = this.value
}


fun <T> List<T>.getItemInList(position: Int): T? {
    if (position < 0 || position >= this.size) {
        return null
    }
    return this[position]
}

fun ConstraintSet.match(view: View, parentView: View) {
    this.connect(view.id, ConstraintSet.TOP, parentView.id, ConstraintSet.TOP)
    this.connect(view.id, ConstraintSet.START, parentView.id, ConstraintSet.START)
    this.connect(view.id, ConstraintSet.END, parentView.id, ConstraintSet.END)
    this.connect(view.id, ConstraintSet.BOTTOM, parentView.id, ConstraintSet.BOTTOM)
}