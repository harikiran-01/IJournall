package com.hk.ijournal.views.diary

import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentSmileyRatingBinding
import com.hk.ijournal.utils.TriangleEdgeEndTreatment

class SmileyRatingFragment : Fragment() {

    private lateinit var fragmentSmileyRatingBinding: FragmentSmileyRatingBinding

    companion object RatingFragFactory {
        const val FRAG_NAME = "rating_frag"
        fun newInstance(): SmileyRatingFragment {
            val smileyRatingFragment = SmileyRatingFragment()
            return smileyRatingFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        fragmentSmileyRatingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_smiley_rating, container, false)
        setTheme()
        return fragmentSmileyRatingBinding.root
    }

    private fun setTheme() {
        fragmentSmileyRatingBinding.dialogfrag.background = MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().apply { setTopEdge(TriangleEdgeEndTreatment(25f, false)) }.build()).apply {
            setStroke(6f, ColorStateList.valueOf(resources.getColor(R.color.colorPrimary)))
            paintStyle = Paint.Style.STROKE
        }
    }

}