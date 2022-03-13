package com.hk.ijournal.common.base

import androidx.annotation.IntDef

const val ITEM_NONE = 0
const val ITEM_MINI_PAGE = 1

// ITEM_DAY_CONTENT = parent view of all the ITEM_DAY_* view types
const val ITEM_DAY_CONTENT = 2
const val ITEM_DAY_TEXT = 3
const val ITEM_DAY_IMAGE = 4

@IntDef(
    ITEM_NONE,
    ITEM_MINI_PAGE,
    ITEM_DAY_TEXT,
    ITEM_DAY_IMAGE
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