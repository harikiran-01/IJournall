package com.hk.ijournal.dayentry.models

import androidx.annotation.StringDef

const val CONTENT_TEXT = "text"
const val CONTENT_IMAGE = "image"

@StringDef(
    CONTENT_TEXT,
    CONTENT_IMAGE)

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)

@Retention(AnnotationRetention.SOURCE)
annotation class EntityType