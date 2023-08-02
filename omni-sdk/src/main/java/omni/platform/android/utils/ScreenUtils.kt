package omni.platform.android.utils

import android.content.Context

object ScreenUtils {
    /**
     * Gets screen width.
     *
     * @param context the context
     * @return the screen width
     */
    fun getScreenWidth(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.widthPixels
    }


    /**
     * Gets screen height.
     *
     * @param context the context
     * @return the screen height
     */
    fun getScreenHeight(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.heightPixels
    }
}