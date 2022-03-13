package com.hk.ijournal.dayentry.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.hk.ijournal.common.match
import com.hk.ijournal.databinding.ImageWithTitleBinding

class PageContentView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = ImageWithTitleBinding.inflate(inflater).root
        val set = ConstraintSet()
        addView(view)

        set.clone(this)
        set.match(view, this)
    }
}