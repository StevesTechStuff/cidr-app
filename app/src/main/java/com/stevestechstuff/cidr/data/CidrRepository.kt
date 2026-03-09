package com.stevestechstuff.cidr.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cidr_prefs")

class CidrRepository(private val context: Context) {

    companion object {
        val LAST_ENTERED_CIDR = stringPreferencesKey("last_entered_cidr")
        // Default realistic starting IP
        const val DEFAULT_CIDR = "192.168.1.0/24"
    }

    val lastEnteredCidrFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LAST_ENTERED_CIDR] ?: DEFAULT_CIDR
    }

    suspend fun saveLastEnteredCidr(cidr: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_ENTERED_CIDR] = cidr
        }
    }
}
