package com.hk.ijournal.views.ui.diary

import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.hk.ijournal.R
import com.hk.ijournal.databinding.FragmentSmileyRatingBinding
import com.hk.ijournal.utils.TriangleEdgeEndTreatment

class SmileyRatingFragment(private val source: View?) : DialogFragment() {

    private lateinit var fragmentSmileyRatingBinding: FragmentSmileyRatingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        fragmentSmileyRatingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_smiley_rating, container, false)

        changeWindowProperties()
        setDialogTheme()
        setDialogPosition()

        return fragmentSmileyRatingBinding.root
    }

    private fun setDialogTheme() {
        fragmentSmileyRatingBinding.dialogfrag.background = MaterialShapeDrawable(ShapeAppearanceModel().toBuilder().apply { setBottomEdge(TriangleEdgeEndTreatment(25f, false)) }.build()).apply {
            fillColor = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            paintStyle = Paint.Style.FILL
        }
    }

    private fun changeWindowProperties() {
        val window = dialog?.window
        //set the dialog to non-modal and disable dim out fragment behind
        //window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun setDialogPosition() {
        if (source == null) return
        // Find out location of source component on screen
        val location = IntArray(2)
        source.getLocationOnScreen(location)
        val sourceX = location[0]
        val sourceY = location[1]

        val window = dialog?.window
        val params = window?.attributes
        //editing params based on source position
        params?.x = sourceX - dpToPx(260f) // about half of confirm button size left of source view
        params?.y = sourceY - dpToPx(70f) // above source view
        // set "origin" to top edge
        window?.apply {
            setGravity(Gravity.TOP)
            attributes = params
        }
    }

    fun dpToPx(valueInDp: Float): Int {
        val metrics = requireActivity().resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics).toInt()
    }

}