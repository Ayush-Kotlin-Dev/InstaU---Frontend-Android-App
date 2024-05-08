package ayush.ggv.instau.util

import androidx.datastore.core.DataStore
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object Utils {
    suspend fun getCurrentUserAndToken(dataStore: DataStore<UserSettings>): Pair<Long, String> {
        val userSettings = dataStore.data.map { it.toAuthResultData() }.first()
        return Pair(userSettings.id, userSettings.token)
    }
}