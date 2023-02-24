package com.nima.openbooksdownloader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(BookTypeConverters::class)
@Database(version = 1, entities = [Book::class, Tag::class])
abstract class BookDatabase: RoomDatabase() {

    abstract fun dao(): BookDao
}