package com.hk.ijournal.utils

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class TriangleEdgeEndTreatment(val size: Float, val inside: Boolean) : EdgeTreatment(), Cloneable {
    override fun getEdgePath(length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        shapePath.lineTo(-size * interpolation, 0f)
        shapePath.lineTo(0f, if (inside) size * interpolation else -size * interpolation)
        shapePath.lineTo(size * interpolation, 0f)
    }
}