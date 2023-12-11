package com.mateusz.onthisday.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mateusz.onthisday.data.db.entity.Favourite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favourite: Favourite)

    @Delete
    suspend fun delete(favourite: Favourite)

    @Query("SELECT * FROM favourites WHERE id = :favouriteId")
    suspend fun getFavouriteByTitle(favouriteId: String?): Favourite


    @Query("SELECT * FROM favourites")
    fun getAll(): Flow<List<Favourite>>

}