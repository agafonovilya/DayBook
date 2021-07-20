package ru.agafonovilya.daybook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import ru.agafonovilya.daybook.model.entity.Lesson
import ru.agafonovilya.daybook.model.repository.IRepository
import java.util.*

class ScheduleViewModel(private val repository: IRepository) : ViewModel() {
    fun loadLessonList(): Flow<List<Lesson>> {
        return repository.getLessons()
    }

    fun findCurrentTimeLessonPosition(list: List<Lesson>): Int {
        val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val pos = 0
        for (i in list.indices) {
            var previousLessonEndTimeHour = 0
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

class ScheduleViewModelFactory(private val repository: IRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
