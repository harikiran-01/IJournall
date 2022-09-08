package bliss.platform.android.components.android

import androidx.annotation.IntDef

const val ITEM_NONE = 0
const val ITEM_MINI_PAGE = 1
const val ITEM_DAY_TEXT = 2
const val ITEM_DAY_IMAGE = 3

@IntDef(
    ITEM_NONE,
    ITEM_MINI_PAGE,
    ITEM_DAY_TEXT,
    ITEM_DAY_IMAGE)

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