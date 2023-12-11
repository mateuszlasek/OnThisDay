package com.mateusz.onthisday.data.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mateusz.onthisday.data.db.entity.Favourite
import com.mateusz.onthisday.repository.FavouriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class FavouriteViewModel(app: Application) : AndroidViewModel(app) {

    private var repositoryFav: FavouriteRepository

    init {
        val favouriteDao = EventDatabase.getDatabase(app).favouriteDao()
        repositoryFav = FavouriteRepository(favouriteDao)

    }

    fun getFavorites(): Flow<List<Favourite>> {
        return repositoryFav.getAll()
    }
    suspend fun getFavouriteByTitle(favouriteTitle: String?): Favourite {
        return repositoryFav.getFavouriteByTitle(favouriteTitle)
    }
    suspend fun addToFavourites(event: Favourite){
        val favouriteQuote = Favourite(
            event.text,
            event.title,
            event.originalimage,
            event.year,
            event.id
        )
        viewModelScope.launch(Dispatchers.IO){
            repositoryFav.insert(favouriteQuote)
        }
    }
    suspend fun removeFromFavourites(event: Favourite){
        val favouriteQuote = Favourite(
            event.text,
            event.title,
            event.originalimage,
            event.year,
            event.id
        )
        repositoryFav.delete(favouriteQuote)
    }

}