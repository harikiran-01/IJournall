package com.hk.ijournal.features.dayentry.models

import java.time.LocalDate

data class MiniPage(var selectedDate: LocalDate, var content: String, var rating: Int)