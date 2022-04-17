package com.hk.ijournal

import com.hk.ijournal.utils.DateConverter.toDate
import com.hk.ijournal.utils.DateConverter.toString
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class SampleUnitTest {

    @Test
    fun isDateConvertedToLocalDate() {
        Assert.assertEquals(LocalDate.of(2022, 1, 1), toDate("2022-01-01"))
    }

    @Test
    fun isLocalDateConvertedToString() {
        Assert.assertEquals("2022-01-01", toString(LocalDate.of(2022, 1, 1)))
    }

    @Test
    fun isModel() {
        val model =
        Assert.assertEquals("2022-01-01", toString(LocalDate.of(2022, 1, 1)))
    }
}