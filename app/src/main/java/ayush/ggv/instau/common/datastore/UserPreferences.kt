package ayush.ggv.instau.common.datastore

 const val PREFERENCES_FILE_NAME = "app_user_settings.preferences_pb"
 interface UserPreferences{
    suspend fun getUserData(): UserSettings
    suspend fun setUserData(userSettings: UserSettings)
}