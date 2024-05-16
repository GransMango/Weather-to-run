package no.uio.ifi.in2000.team53.weatherapp.data.remote.activitydatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.model.remote.activitydatabase.ActivityInformation
import javax.inject.Inject


/**
 * Repository for managing activities in the remote database.
 *
 * @property dataSource The data source for activities.
 */
class ActivityDatabaseRepository @Inject constructor(
    private val dataSource: ActivityDatabaseDatasource,
) {

    private val _remoteActivities = MutableStateFlow<List<ActivityInformation>>(emptyList())
    private val remoteActivities: StateFlow<List<ActivityInformation>> = _remoteActivities.asStateFlow()


    /**
     * Loads all activities from the remote database and updates the state.
     */
    suspend fun loadActivities() {
        val activities = dataSource.getActivities()
        _remoteActivities.value = activities
    }

    /**
     * Returns a flow of all user activities from the local database.
     *
     * @return Flow of list of [UserActivityEntity] objects.
     */
    fun observeActivities(): Flow<List<ActivityInformation>> {
        return remoteActivities
    }
}