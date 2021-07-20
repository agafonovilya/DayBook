package ru.agafonovilya.daybook.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lesson (
    val title: String,
    val timeStart: String,
    val timeEnd: String,
    val image: String,
    val isOptional: Boolean,
    val optionalDescription: String?,
    val isOnline: Boolean
): Parcelable