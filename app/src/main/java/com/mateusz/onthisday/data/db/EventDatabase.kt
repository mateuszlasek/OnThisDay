package com.mateusz.onthisday.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter

import androidx.room.TypeConverters
import com.mateusz.onthisday.Converters
import com.mateusz.onthisday.data.db.dao.FavouriteDao
import com.mateusz.onthisday.data.db.entity.Favourite
import retrofit2.Converter

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