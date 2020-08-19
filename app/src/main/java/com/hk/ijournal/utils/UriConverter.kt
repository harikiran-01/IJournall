package com.hk.ijournal.utils

import android.net.Uri
import androidx.room.TypeConverter

object UriConverter {
    @JvmStatic
    @TypeConverter
    fun stringToUri(string: String?): Uri {
        return Uri.parse(string)
    }

    @JvmStatic
    @TypeConverter
    fun uriToString(uri: Uri?): String {
        return uri.toString()
    }
}