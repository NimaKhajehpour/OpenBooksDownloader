package com.nima.openbooksdownloader.database

import androidx.room.TypeConverter

class BookTypeConverters {

    @TypeConverter
    fun fromBoolean(bool: Boolean): Int = when(bool){
        false -> 0
        else -> 1
    }

    @TypeConverter
    fun toBoolean(bool: Int): Boolean = when(bool){
        0 -> false
        else -> true
    }
}