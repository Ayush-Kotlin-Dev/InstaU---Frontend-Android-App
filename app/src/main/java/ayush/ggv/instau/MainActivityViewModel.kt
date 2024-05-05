package ayush.ggv.instau

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import kotlinx.coroutines.flow.map

class MainActivityViewModel(
    private val datastore : DataStore<UserSettings>
) : ViewModel() {

    val authState = datastore.data.map { it.toAuthResultData().token }
    val userId = datastore.data.map { it.toAuthResultData().id }
}