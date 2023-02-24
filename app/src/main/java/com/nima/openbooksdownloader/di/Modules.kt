package com.nima.openbooksdownloader.di

import android.content.Context
import androidx.room.Room
import com.nima.openbooksdownloader.database.BookDao
import com.nima.openbooksdownloader.database.BookDatabase
import com.nima.openbooksdownloader.network.OpenBooksAPI
import com.nima.openbooksdownloader.repository.OpenBookRepository
import com.nima.openbooksdownloader.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    @Singleton
    fun provideApi(): OpenBooksAPI = Retrofit.Builder().baseUrl(Constants.baseUrl).addConverterFactory(
        GsonConverterFactory.create()
    ).build().create(OpenBooksAPI::class.java)

    @Provides
    @Singleton
    fun provideRepository(api: OpenBooksAPI, dao: BookDao) = OpenBookRepository(api = api, dao = dao)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookDatabase =
        Room.databaseBuilder(context,
            BookDatabase::class.java,
            "BookDatabase"
        ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideDao(database: BookDatabase): BookDao = database.dao()

}