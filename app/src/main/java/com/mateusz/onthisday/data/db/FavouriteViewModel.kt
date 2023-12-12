package com.mateusz.onthisday.data.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mateusz.onthisday.data.models.Favourite
import com.mateusz.onthisday.repository.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    app: Application
) : AndroidViewModel(app) {

    private var repositoryFav: FavouriteRepository

    init {
        val favouriteDao = EventDatabase.getDatabase(app).favouriteDao()
        repositoryFav = FavouriteRepository(favouriteDao)

    }

    fun getFavorites(): Flow<List<Favourite>> {
        return repositoryFav.getAll()
    }
//    suspend fun isFavouriteExists(text: String?): Boolean {
//        return repositoryFav.isFavouriteExists(text)
//    }
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