package com.nima.openbooksdownloader.di

import android.content.Context
import androidx.room.Room
import com.nima.openbooksdownloader.database.BookDao
import com.nima.openbooksdownloader.database.BookDatabase
import com.nima.openbooksdownloader.network.OpenBooksAPI
import com.nima.openbooksdownloader.repository.OpenBookRepository
import com.nima.openbooksdownloader.utils.Constants
import com.nima.openbooksdownloader.viewmodel.BookViewModel
import com.nima.openbooksdownloader.viewmodel.BookmarkViewModel
import com.nima.openbooksdownloader.viewmodel.DownloadsViewModel
import com.nima.openbooksdownloader.viewmodel.HomeViewModel
import com.nima.openbooksdownloader.viewmodel.MainViewModel
import com.nima.openbooksdownloader.viewmodel.SavedBookViewModel
import com.nima.openbooksdownloader.viewmodel.SearchViewModel
import com.nima.openbooksdownloader.viewmodel.TagViewModel
import com.nima.openbooksdownloader.viewmodel.TagsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val module = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            BookDatabase::class.java,
            "BookDatabase"
        ).fallbackToDestructiveMigration(false).build()
    }

    single {
        val database = get<BookDatabase>()
        database.dao()
    }

    single {
        Retrofit.Builder().baseUrl(Constants.baseUrl).addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(OpenBooksAPI::class.java)
    }

    single {
        OpenBookRepository(get(), get())
    }

    viewModel {
        BookmarkViewModel(get())
    }
    viewModel {
        BookViewModel(get())
    }
    viewModel {
        DownloadsViewModel(get())
    }
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        SavedBookViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        TagsViewModel(get())
    }
    viewModel {
        TagViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }
}