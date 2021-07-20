package ru.agafonovilya.daybook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.agafonovilya.daybook.model.entity.Homework
import ru.agafonovilya.daybook.model.entity.Lesson
import ru.agafonovilya.daybook.model.repository.IRepository
import java.util.*

class HomeViewModel(private val repository: IRepository) : ViewModel() {

    fun getExamsDate(): Flow<Long> {
        val examDate = repository.getExamsDate().value
        var countDownTime = getCountDownTime(examDate)

        return flow {
            while (countDownTime > 0) {
                emit(countDownTime)
                countDownTime -= 1000
                delay(1000)
            }
            emit(0)
        }
    }

    private fun getCountDownTime(examDate: Long): Long {
        val currentTime = Calendar.getInstance().timeInMillis
        return examDate - currentTime
    }

    fun loadHomeworkList(): Flow<List<Homework>> {
        return repository.getHomeworkList()
    }

    fun loadLessonList(): Flow<List<Lesson>> {
        return repository.getLessons()
    }

    fun findCurrentTimeLessonPosition(list: List<Lesson>): Int {
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val pos: Int = 0
        for (i in list.indices) {
            var previousLessonEndTimeHour: Int = 0
            val lessonStartTimeHour = list[i].timeStart.subSequence(0, 2).toString().toInt()
            if (i > 0) previousLessonEndTimeHour =
                list[i - 1].timeEnd.subSequence(0, 2).toString().toInt()
            if (lessonStartTimeHour == currentTime) {
                return i
            } else if (lessonStartTimeHour > currentTime && i > 0 && previousLessonEndTimeHour < currentTime) return i
            else if (lessonStartTimeHour > currentTime && i > 0 && previousLessonEndTimeHour >= currentTime) return i - 1
        }
        return pos
    }

}

class HomeViewModelFactory(private val repository: IRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

