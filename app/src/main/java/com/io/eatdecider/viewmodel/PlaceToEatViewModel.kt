package com.io.eatdecider.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.io.eatdecider.R
import com.io.eatdecider.database.Repository
import com.io.eatdecider.database.dao.DatabaseDao
import com.io.eatdecider.models.Place
import com.io.eatdecider.models.PreviousPlace
import com.io.eatdecider.util.*
import kotlinx.coroutines.launch

class PlaceToEatViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    private val _data = MutableLiveData<ViewState>()
    val data: LiveData<ViewState> = _data

    init {
        val productDb = DatabaseDao.getInstance(application).dao()
        repository = Repository(productDb)
        viewModelScope.launch {
            _data.value = ViewState.HistoryRetrievedSuccessfully(repository.getHistory())

        }
    }

    fun setLoaded() {
        _data.value = ViewState.Loaded
    }

    fun getRandomPlace(): Place {
        val placePicked = listOfPlaces.random()

        viewModelScope.launch {
            repository.addToHistory(placePicked)
        }

        return Place(placePicked.imageId, placePicked.placeName)
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
            _data.value = ViewState.HistoryDeletedSuccessfully
        }
    }
}

sealed class ViewState {
    data class HistoryRetrievedSuccessfully(val history: MutableList<PreviousPlace>) : ViewState()
    object HistoryDeletedSuccessfully : ViewState()
    object Loading : ViewState()
    object Loaded : ViewState()
}


private val listOfPlaces = listOf(
    Place(R.drawable.ic_chinese, CHINESE_FOOD),
    Place(R.drawable.ic_mall, MALL),
    Place(R.drawable.ic_boxes, BOXES),
    Place(R.drawable.ic_grill, GRILL),
    Place(R.drawable.ic_burgers, BURGERS),
    Place(R.drawable.ic_pizza, PIZZA)
)