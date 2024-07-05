package ayush.ggv.instau.data.onboarding.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import ayush.ggv.instau.data.onboarding.domain.DataStoreOperations
import ayush.ggv.instau.util.Constants.PREFERENCES_KEY
import ayush.ggv.instau.util.Constants.PREFERENCES_NAME

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreOperationImpl(
    context: Context
) : DataStoreOperations {

    private object PreferenceKeys {
        val onBoardingKey = booleanPreferencesKey(name = PREFERENCES_KEY)
    }

    private val dataStore = context.dataStore
    override suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.onBoardingKey] = completed
        }
    }

    override fun getOnBoardingState(): Flow<Boolean> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
            .map { preferences ->
                val onBoardingCompleted = preferences[PreferenceKeys.onBoardingKey] ?: false
                onBoardingCompleted
            }
    }
}