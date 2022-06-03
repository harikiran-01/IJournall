package com.hk.ijournal.features.calendar.views

import android.view.View
import com.hk.ijournal.databinding.CalendarMonthHeaderLayoutBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class MonthViewContainer(view: View) : ViewContainer(view) {
    val textView = CalendarMonthHeaderLayoutBinding.bind(view).headerTextView
}