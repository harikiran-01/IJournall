package com.hk.ijournal.utils

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class TriangleEdgeEndTreatment(private val size: Float, private val inside: Boolean) : EdgeTreatment(), Cloneable {
    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        shapePath.lineTo(length - size * interpolation, 0f)
        shapePath.lineTo(length, if (inside) size * interpolation else -size * interpolation)
    }
}