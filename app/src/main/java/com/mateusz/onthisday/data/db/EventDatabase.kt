package com.mateusz.onthisday.data.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mateusz.onthisday.data.db.local.FavouriteDao
import com.mateusz.onthisday.data.models.Favourite
import dagger.hilt.android.HiltAndroidApp

@Database(version = 1, entities = [Favourite::class], exportSchema = false)
abstract class EventDatabase: RoomDatabase(){
    abstract fun favouriteDao(): FavouriteDao

    companion object{
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "events_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}