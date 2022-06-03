package com.hk.ijournal.features.calendar.views

import android.view.View
import com.hk.ijournal.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate

class DayViewContainer(view: View, dayListener: (LocalDate) -> Unit) : ViewContainer(view) {
    val calendarDayTxt = CalendarDayLayoutBinding.bind(view).calendarDayText
    // Will be set when this container is bound
    lateinit var day: CalendarDay

    init {
        view.setOnClickListener {
            dayListener.invoke(day.date)
        }
    }
}