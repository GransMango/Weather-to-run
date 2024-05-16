package no.uio.ifi.in2000.team53.weatherapp.di
import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.team53.weatherapp.data.local.location.LocationDatasource
import no.uio.ifi.in2000.team53.weatherapp.data.local.location.LocationRepository
import no.uio.ifi.in2000.team53.weatherapp.data.local.preferences.UserPreferencesManager
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.LocalActivitiesDatabase
import no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities.UserActivityRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.activitydatabase.ActivityDatabaseDatasource
import no.uio.ifi.in2000.team53.weatherapp.data.remote.activitydatabase.ActivityDatabaseRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.pollenapi.PollenAPIDatasource
import no.uio.ifi.in2000.team53.weatherapp.data.remote.pollenapi.PollenAPIRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.weatherapi.MetAPIRepository
import no.uio.ifi.in2000.team53.weatherapp.data.remote.weatherapi.MetApiDatasource
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityDao
import no.uio.ifi.in2000.team53.weatherapp.model.local.userinfo.UserDao
import no.uio.ifi.in2000.team53.weatherapp.model.local.usertimesdb.UserTimesDao
import javax.inject.Singleton

/**
 * Dagger module that provides all dependencies for the application.
 * All dependencies provided here are scoped to the application's lifecycle.
 * By using the @InstallIn annotation, we specify that the dependencies provided by this module
 * should be available in the SingletonComponent, which is the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the MetApiDatasource instance.
     * @return the MetApiDatasource instance.
     */
    @Singleton
    @Provides
    fun provideMetDataSource(): MetApiDatasource = MetApiDatasource()

    /**
     * Provides the MetAPIRepository instance.
     * @param metApiDatasource the data source for fetching weather data from a remote API.
     * @return the MetAPIRepository instance.
     */
    @Singleton
    @Provides
    fun provideMetRepository(metApiDatasource: MetApiDatasource): MetAPIRepository = MetAPIRepository(metApiDatasource)

    /**
     * Provides the FusedLocationProviderClient instance.
     * @param context the application context.
     * @return the FusedLocationProviderClient instance.
     */
    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * Provides the LocationDataSource instance.
     * @param application the application instance.
     * @param fusedLocationProviderClient the client for accessing location services.
     * @return the LocationDataSource instance.
     */
    @Singleton
    @Provides
    fun provideLocationDataSource(
        application: Application, // Direct use of Application instance
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationDatasource = LocationDatasource(application, fusedLocationProviderClient)

    /**
     * Provides the LocationRepository instance.
     * @param locationDataSource the data source for fetching location data.
     * @return the LocationRepository instance.
     */
    @Singleton
    @Provides
    fun provideLocationRepository(
        locationDataSource: LocationDatasource,
        userPreferencesManager: UserPreferencesManager
    ): LocationRepository = LocationRepository(locationDataSource, userPreferencesManager)

    /**
     * Provides the UserPreferencesManager instance.
     * @param context the application context.
     * @return the UserPreferencesManager instance.
     */
    @Singleton
    @Provides
    fun provideUserPreferencesManager(@ApplicationContext context: Context): UserPreferencesManager {
        return UserPreferencesManager(context)
    }

    /**
     * Provides the ActivityDatabaseDataSource instance.
     * @return the ActivityDatabaseDataSource instance.
     */
    @Singleton
    @Provides
    fun provideActivityDatabaseDataSource(): ActivityDatabaseDatasource = ActivityDatabaseDatasource()

    /**
     * Provides the ActivityDatabaseRepository instance.
     * @param dataSource the data source for fetching activity data from a remote API.
     * @return the ActivityDatabaseRepository instance.
     */
    @Singleton
    @Provides
    fun provideActivityDatabaseRepository(dataSource: ActivityDatabaseDatasource): ActivityDatabaseRepository {
        return ActivityDatabaseRepository(dataSource)
    }

    /**
     * Provides the UserActivityRepository instance.
     * @param userActivityDao the DAO for accessing user activity data.
     * @return the UserActivityRepository instance.
     */
    @Singleton
    @Provides
    fun provideUserActivityRepository(userActivityDao: UserActivityDao): UserActivityRepository {
        return UserActivityRepository(userActivityDao)
    }

    /**
     * Provides the LocalActivitiesDatabase instance.
     * @param context the application context.
     * @return the LocalActivitiesDatabase instance.
     */
    @Singleton
    @Provides
    fun provideLocalActivitiesDatabase(@ApplicationContext context: Context): LocalActivitiesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalActivitiesDatabase::class.java,
            "local_activities_database"
        ).fallbackToDestructiveMigration().build()
    }

    /**
     * Provides the PollenAPIDatasource instance.
     * @return the PollenAPIDatasource instance.
     */
    @Singleton
    @Provides
    fun providePollenAPIDataSource(): PollenAPIDatasource = PollenAPIDatasource()

    /**
     * Provides the PollenAPIRepository instance.
     * @param pollenAPIDatasource the data source for fetching pollen data from a remote API.
     * @return the PollenAPIRepository instance.
     */
    @Singleton
    @Provides
    fun providePollenAPIRepository(pollenAPIDatasource: PollenAPIDatasource) : PollenAPIRepository = PollenAPIRepository(pollenAPIDatasource)

    /**
     * Provides the UserActivityDao instance.
     * @param database the local activities database.
     * @return the UserActivityDao instance.
     */
    @Singleton
    @Provides
    fun provideUserActivityDao(database: LocalActivitiesDatabase): UserActivityDao {
        return database.userActivityDao()
    }

    /**
     * Provides the UserDao instance.
     * @param database the local activities database.
     * @return the UserDao instance.
     */
    @Singleton
    @Provides
    fun provideUserDao(database: LocalActivitiesDatabase): UserDao {
        return database.userDao()
    }

    /**
     * Provides the UserTimesDao instance.
     * @param database the local activities database.
     * @return the UserTimesDao instance.
     */
    @Singleton
    @Provides
    fun provideUserTimesDao(database: LocalActivitiesDatabase): UserTimesDao {
        return database.userTimesDao()
    }

}