package ru.agafonovilya.daybook.utils

interface IImageLoader<T> {
    fun loadInto(url: String, container: T)
}