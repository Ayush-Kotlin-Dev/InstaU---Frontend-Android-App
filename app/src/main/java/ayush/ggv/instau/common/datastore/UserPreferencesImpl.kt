package ayush.ggv.instau.common.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first

class UserPreferencesImpl(
    private val dataStore: DataStore<UserSettings>
): UserPreferences{
    override suspend fun getUserData(): UserSettings {
        return dataStore.data.first()
    }

    override suspend fun setUserData(userSettings: UserSettings) {
        dataStore.updateData { userSettings }
    }
}