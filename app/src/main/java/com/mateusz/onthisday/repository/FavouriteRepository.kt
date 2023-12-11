package com.mateusz.onthisday.repository

import com.mateusz.onthisday.data.db.dao.FavouriteDao
import com.mateusz.onthisday.data.db.entity.Favourite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

    fun getAll(): Flow<List<Favourite>> {
        return favouriteDao.getAll()
    }

    suspend fun insert(favourite: Favourite) = withContext(Dispatchers.IO) {
        favouriteDao.insert(favourite)
    }
    suspend fun getFavouriteByTitle(favouriteTitle: String?): Favourite = withContext(Dispatchers.IO){
        favouriteDao.getFavouriteByTitle(favouriteTitle)
    }

    suspend fun delete(favourite: Favourite) = withContext(Dispatchers.IO) {
        favouriteDao.delete(favourite)
    }

}