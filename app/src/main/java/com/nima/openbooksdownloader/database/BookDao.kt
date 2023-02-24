package com.nima.openbooksdownloader.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("select * from tag")
    fun getAllTags(): Flow<List<Tag>>

    @Query("select * from book where tag = :tag")
    fun getBookByTag(tag: String): Flow<List<Book>>

    @Query("select * from book where id =:id")
    fun getBookById(id: String): Flow<Book>

    @Query("select * from book")
    fun getSavedBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBook(book: Book)
}