package com.nima.openbooksdownloader.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Book(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val note: String,
    @ColumnInfo
    val tag: String
)