package com.hk.ijournal.repository.models

enum class ContentType {
    TYPED, LOADED
}

data class Content(val text: String, var contentType: ContentType)