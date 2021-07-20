package ru.agafonovilya.daybook.model.repository

import kotlinx.coroutines.flow.StateFlow
import ru.agafonovilya.daybook.model.entity.Homework
import ru.agafonovilya.daybook.model.entity.Lesson

interface IRepository {
    fun getLessons(): StateFlow<List<Lesson>>
    fun getHomeworkList(): StateFlow<List<Homework>>
    fun getExamsDate(): StateFlow<Long>
}