package com.hk.ijournal.calendar.views

import android.util.Log
import android.view.View
import com.hk.ijournal.calendar.viewmodel.CalendarViewModel
import com.hk.ijournal.common.base.BaseFragment
import com.hk.ijournal.databinding.CalendarFragmentBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@AndroidEntryPoint
class CalendarFragment : BaseFragment<CalendarFragmentBinding, CalendarViewModel>() {

    val daySelected: ((date : LocalDate) -> Unit) = {
        Log.d("CalTest", it.toString())
    }

    override fun getViewModelClass(): Class<CalendarViewModel> {
        return CalendarViewModel::class.java
    }

    override fun getViewBinding(): CalendarFragmentBinding {
        return CalendarFragmentBinding.inflate(layoutInflater)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view, daySelected)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                // Set the calendar day for this container.
                container.day = day
                container.calendarDayTxt.text = day.date.dayOfMonth.toString()
            }
        }

        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text =
                    "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(firstMonth, currentMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)
    }
}