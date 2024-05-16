package no.uio.ifi.in2000.team53.weatherapp.data.local.useractivities

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityDao
import no.uio.ifi.in2000.team53.weatherapp.model.local.useractivitydatabase.UserActivityEntity
import no.uio.ifi.in2000.team53.weatherapp.model.local.userinfo.UserDao
import no.uio.ifi.in2000.team53.weatherapp.model.local.userinfo.UserEntity
import no.uio.ifi.in2000.team53.weatherapp.model.local.usertimesdb.UserTimesDao
import no.uio.ifi.in2000.team53.weatherapp.model.local.usertimesdb.UserTimesEntity
import no.uio.ifi.in2000.team53.weatherapp.utilities.TimePairConverter

/**
 * The Room database for storing user activities.
 * | Entity | Description |
 * | --- | --- |
 * | UserActivityEntity | Represents a user activity. |
 * | UserEntity | Represents a user. |
 * | UserTimesEntity | Represents the user's activity times. |
 */
@Database(entities = [UserActivityEntity::class,UserEntity::class,UserTimesEntity::class],  version = 25)
@TypeConverters(TimePairConverter::class)
abstract class LocalActivitiesDatabase : RoomDatabase() {
    /**
     * Gets the UserActivityDao instance.
     * @return the UserActivityDao instance.
     */
    abstract fun userActivityDao(): UserActivityDao

    /**
     * Gets the UserDao instance.
     * @return the UserDao instance.
     */
    abstract fun userDao(): UserDao

    /**
     * Gets the UserTimesDao instance.
     * @return the UserTimesDao instance.
     */
    abstract fun userTimesDao(): UserTimesDao

}
