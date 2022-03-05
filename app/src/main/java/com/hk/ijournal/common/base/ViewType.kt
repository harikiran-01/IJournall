package com.hk.ijournal.common.base

import androidx.annotation.IntDef

const val ITEM_NONE = 0
const val ITEM_MINI_PAGE = 1

@IntDef(
    ITEM_NONE,
    ITEM_MINI_PAGE
)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.SOURCE)
annotation class ViewType