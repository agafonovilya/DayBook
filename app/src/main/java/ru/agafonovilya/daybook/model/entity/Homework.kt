package ru.agafonovilya.daybook.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Homework (
    val lesson: String,
    val deadline: String,
    val image: String,
    val description: String
    ): Parcelable