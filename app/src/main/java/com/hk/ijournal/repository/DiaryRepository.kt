package com.hk.ijournal.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.hk.ijournal.repository.local.DiaryDao
import com.hk.ijournal.repository.models.DiaryPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class DiaryRepository(private val diaryDao: DiaryDao, private val userId: Long, private val coroutineScope: CoroutineScope) {
    val saveStatus: MutableLiveData<String> = MutableLiveData()

    val selectedDateLive: MutableLiveData<LocalDate> = MutableLiveData()

    private lateinit var currentPage: DiaryPage
    private var dbPage: DiaryPage? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private var diaryPage: MutableLiveData<DiaryPage> = Transformations.map(selectedDateLive) {
        dbPage ?: DiaryPage(it)
    } as MutableLiveData<DiaryPage>

    @RequiresApi(Build.VERSION_CODES.O)
    val pageContentLive: MutableLiveData<String> = Transformations.map(diaryPage) {
        println("pagedeb ${it.selectedDate} ${it.content} ")
        it.content
    } as MutableLiveData<String>

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            loadNewPage(LocalDate.now())
        }
    }

    private fun loadNewPage(selectedDate: LocalDate) {
        saveStatus.value = ""
        coroutineScope.launch {
            dbPage = getPagefromDB(selectedDate, userId)
            selectedDateLive.value = selectedDate
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectPrevDate() {
        selectedDateLive.value?.minusDays(1)?.let {
            loadNewPage(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectNextDate() {
        selectedDateLive.value?.plusDays(1)?.let {
            loadNewPage(it)
        }
    }

    fun savePage(typedContent: String, stoppedTyping: Boolean) {
        if (!stoppedTyping)
            updateSaveStatustoTyping()
        else {
            currentPage = DiaryPage(selectedDateLive.value!!, userId, typedContent, 0)
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    currentPage.apply {
                        dbPage?.let {
                            pid = it.pid
                            diaryDao.updatePage(this)
                        } ?: run {
                            pid = diaryDao.insertPage(this)
                            dbPage = currentPage
                        }
                    }
                }
            }
            saveStatus.value = "Page Saved"
        }
    }

    private suspend fun getPagefromDB(selectedDate: LocalDate, userId: Long) = withContext(Dispatchers.IO) {
        diaryDao.getPageforDate(selectedDate, userId)
    }

    private fun updateSaveStatustoTyping() {
        saveStatus.value = "Typing ..."
    }

}