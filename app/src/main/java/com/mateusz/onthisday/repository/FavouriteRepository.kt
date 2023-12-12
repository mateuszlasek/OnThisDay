package com.mateusz.onthisday.repository

import com.mateusz.onthisday.data.db.local.FavouriteDao
import com.mateusz.onthisday.data.models.Favourite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteRepository @Inject constructor(
    private val favouriteDao: FavouriteDao
) {

    fun getAll(): Flow<List<Favourite>> {
        return favouriteDao.getAll()
    }

    suspend fun insert(favourite: Favourite) = withContext(Dispatchers.IO) {
        favouriteDao.insert(favourite)
    }
//    suspend fun isFavouriteExists(text: String?): Boolean = withContext(Dispatchers.IO){
//        favouriteDao.isFavouriteExists(text)
//    }

    suspend fun delete(favourite: Favourite) = withContext(Dispatchers.IO) {
        favouriteDao.delete(favourite)
    }

}