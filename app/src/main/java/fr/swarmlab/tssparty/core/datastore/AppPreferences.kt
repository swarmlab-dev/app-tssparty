package fr.swarmlab.tssparty.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface AppPreferences {
    val partyBusUrl: Flow<String>
}

val PARTYBUS_URL = stringPreferencesKey("partybus_url")

class AppPreferencesImpl(context: Context) : AppPreferences {
    private val dataStore: DataStore<Preferences> = context.dataStore

    override val partyBusUrl: Flow<String> = dataStore.data.map { preferences ->
        preferences[PARTYBUS_URL] ?: "192.168.1.28:8080"
    }
}