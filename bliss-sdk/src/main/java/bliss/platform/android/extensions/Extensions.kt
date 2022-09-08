package bliss.platform.android.extensions

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