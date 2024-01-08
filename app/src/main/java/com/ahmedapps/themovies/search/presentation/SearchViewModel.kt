package com.ahmedapps.themovies.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedapps.themovies.main.data.remote.api.MediaApi.Companion.API_KEY
import com.ahmedapps.themovies.main.domain.repository.MediaRepository
import com.ahmedapps.themovies.search.domain.repository.SearchRepository
import com.ahmedapps.themovies.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _searchScreenState = MutableStateFlow(SearchScreenState())
    val searchScreenState = _searchScreenState.asStateFlow()

    private var searchJob: Job? = null

    fun onEvent(event: SearchUiEvents) {
        when (event) {

            is SearchUiEvents.OnSearchedItemClick -> {
                viewModelScope.launch {
                    mediaRepository.insertItem(event.media)
                }
            }

            is SearchUiEvents.OnSearchQueryChanged -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)

                    _searchScreenState.update {
                        it.copy(
                            searchQuery = event.query,
                            searchList = emptyList()
                        )
                    }

                    loadSearchList()
                }
            }

            is SearchUiEvents.OnPaginate -> {
                _searchScreenState.update {
                    it.copy(
                        searchPage = searchScreenState.value.searchPage + 1
                    )
                }
                loadSearchList()
            }

            is SearchUiEvents.Refresh -> TODO()
        }
    }

    private fun loadSearchList() {

        viewModelScope.launch {

            searchRepository
                .getSearchList(
                    fetchFromRemote = true,
                    query = searchScreenState.value.searchQuery,
                    page = searchScreenState.value.searchPage,
                    apiKey = API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->
                                _searchScreenState.update {
                                    it.copy(
                                        searchList = searchScreenState.value.searchList + mediaList
                                    )
                                }

                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _searchScreenState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

}






