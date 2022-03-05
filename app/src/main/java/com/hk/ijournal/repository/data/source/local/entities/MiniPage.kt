package com.hk.ijournal.repository.data.source.local.entities

import java.time.LocalDate

data class MiniPage(var selectedDate: LocalDate, var content: String, var rating: Int)