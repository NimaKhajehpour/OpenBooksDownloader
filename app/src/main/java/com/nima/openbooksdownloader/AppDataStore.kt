package com.nima.openbooksdownloader

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore (private val context: Context) {
    companion object{

        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("theme")

        val themeKey = booleanPreferencesKey("app_theme")
    }

    val getTheme: Flow<Boolean?> = context.datastore.data
        .map {
            it[themeKey] ?: false
        }

    suspend fun saveTheme(isDark: Boolean){
        context.datastore.edit {
            it[themeKey] = isDark
        }
    }
}