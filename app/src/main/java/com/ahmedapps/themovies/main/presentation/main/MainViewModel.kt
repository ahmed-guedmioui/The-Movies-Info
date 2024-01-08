package com.ahmedapps.themovies.main.presentation.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedapps.themovies.main.data.remote.api.MediaApi.Companion.API_KEY
import com.ahmedapps.themovies.main.domain.models.Media
import com.ahmedapps.themovies.main.domain.repository.GenreRepository
import com.ahmedapps.themovies.main.domain.repository.MediaRepository
import com.ahmedapps.themovies.util.Constants
import com.ahmedapps.themovies.util.Constants.ALL
import com.ahmedapps.themovies.util.Constants.MOVIE
import com.ahmedapps.themovies.util.Constants.NOW_PLAYING
import com.ahmedapps.themovies.util.Constants.POPULAR
import com.ahmedapps.themovies.util.Constants.TOP_RATED
import com.ahmedapps.themovies.util.Constants.TRENDING_TIME
import com.ahmedapps.themovies.util.Constants.TV
import com.ahmedapps.themovies.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {


    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    val showSplashScreen = mutableStateOf(true)

    init {
        load()
        viewModelScope.launch {
            delay(500)
            showSplashScreen.value = false
        }
    }

    private fun load(fetchFromRemote: Boolean = false) {
        loadPopularMovies(fetchFromRemote)
        loadTopRatedMovies(fetchFromRemote)
        loadNowPlayingMovies(fetchFromRemote)

        loadTopRatedTvSeries(fetchFromRemote)
        loadPopularTvSeries(fetchFromRemote)

        loadTrendingAll(fetchFromRemote)

        loadGenres(
            fetchFromRemote = fetchFromRemote,
            isMovies = true
        )
        loadGenres(
            fetchFromRemote = fetchFromRemote,
            isMovies = false
        )
    }


    fun onEvent(event: MainUiEvents) {
        when (event) {

            is MainUiEvents.Refresh -> {

                _mainUiState.update {
                    it.copy(
                        isLoading = true
                    )
                }

                loadGenres(
                    fetchFromRemote = true,
                    isMovies = true
                )

                loadGenres(
                    fetchFromRemote = true,
                    isMovies = false
                )

                when (event.type) {

                    Constants.homeScreen -> {

                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.popularScreen -> {
                        loadPopularMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.trendingAllListScreen -> {
                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.tvSeriesScreen -> {
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.topRatedAllListScreen -> {
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )

                        // calling it to have equal top rated and tv series items,
                        // because loadTopRatedMovies() and loadPopularTvSeries() are already
                        // used to create the tvSeries list. So if i don't call it here I we will
                        // have more TopRated items than Popular ones.
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    Constants.recommendedListScreen -> {
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                }
            }

            is MainUiEvents.OnPaginate -> {

                when (event.type) {

                    Constants.trendingAllListScreen -> {
                        loadTrendingAll(true)
                    }

                    Constants.topRatedAllListScreen -> {
                        loadTopRatedMovies(true)
                        loadTopRatedTvSeries(true)

                        // calling it to have equal top rated and tv series items,
                        // because loadTopRatedMovies() and loadPopularTvSeries() are already
                        // used to create the tvSeries list. So if i don't call it here I we will
                        // have more TopRated items than Popular ones.
                        loadPopularTvSeries(true)
                    }

                    Constants.popularScreen -> {

                        Timber.tag(Constants.GET_TAG).d("onOnPaginate: popularScreen")
                        loadPopularMovies(true)
                    }

                    Constants.tvSeriesScreen -> {
                        loadPopularTvSeries(true)
                        loadTopRatedTvSeries(true)
                    }

                    Constants.recommendedListScreen -> {
                        loadNowPlayingMovies(true)
                        loadTopRatedTvSeries(true)
                    }

                }
            }
        }
    }

    private fun loadGenres(
        fetchFromRemote: Boolean,
        isMovies: Boolean
    ) {
        viewModelScope.launch {

            if (isMovies) {
                genreRepository
                    .getGenres(fetchFromRemote, MOVIE, API_KEY)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { genresList ->
                                    _mainUiState.update {
                                        it.copy(
                                            moviesGenresList = genresList
                                        )
                                    }
                                }
                            }

                            is Resource.Error -> Unit

                            is Resource.Loading -> Unit
                        }
                    }
            } else {
                genreRepository
                    .getGenres(fetchFromRemote, TV, API_KEY)
                    .collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                result.data?.let { genresList ->
                                    _mainUiState.update {
                                        it.copy(
                                            tvGenresList = genresList
                                        )
                                    }
                                }
                            }

                            is Resource.Error -> Unit

                            is Resource.Loading -> Unit
                        }
                    }
            }


        }
    }


    private fun loadPopularMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    POPULAR,
                    mainUiState.value.popularMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            popularMoviesList = shuffledMediaList.toList(),
                                            popularMoviesPage = 1
                                        )
                                    }
                                } else {

                                    _mainUiState.update {
                                        it.copy(
                                            popularMoviesList =
                                            mainUiState.value.popularMoviesList + shuffledMediaList.toList(),
                                            popularMoviesPage = mainUiState.value.popularMoviesPage + 1
                                        )
                                    }
                                }

                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }


    private fun loadTopRatedMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    TOP_RATED,
                    mainUiState.value.topRatedMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedMoviesList = shuffledMediaList.toList(),
                                            topRatedMoviesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedMoviesList =
                                            mainUiState.value.topRatedMoviesList + shuffledMediaList.toList(),
                                            topRatedMoviesPage = mainUiState.value.topRatedMoviesPage + 1
                                        )
                                    }
                                }

                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }


    private fun loadNowPlayingMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    MOVIE,
                    NOW_PLAYING,
                    mainUiState.value.nowPlayingMoviesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            nowPlayingMoviesList = shuffledMediaList.toList(),
                                            nowPlayingMoviesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            nowPlayingMoviesList =
                                            mainUiState.value.nowPlayingMoviesList + shuffledMediaList.toList(),
                                            nowPlayingMoviesPage = mainUiState.value.nowPlayingMoviesPage + 1
                                        )
                                    }
                                }

                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadTopRatedTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    TV,
                    TOP_RATED,
                    mainUiState.value.topRatedTvSeriesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()


                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedTvSeriesList = shuffledMediaList.toList(),
                                            topRatedTvSeriesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedTvSeriesList =
                                            mainUiState.value.topRatedTvSeriesList + shuffledMediaList.toList(),
                                            topRatedTvSeriesPage = mainUiState.value.topRatedTvSeriesPage + 1
                                        )
                                    }
                                }


                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadPopularTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {


            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote,
                    isRefresh,
                    TV,
                    POPULAR,
                    mainUiState.value.popularTvSeriesPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()


                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            popularTvSeriesList = shuffledMediaList.toList(),
                                            popularTvSeriesPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            popularTvSeriesList =
                                            mainUiState.value.popularTvSeriesList + shuffledMediaList.toList(),
                                            popularTvSeriesPage = mainUiState.value.popularTvSeriesPage + 1
                                        )
                                    }
                                }


                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadTrendingAll(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ) {

        viewModelScope.launch {

            mediaRepository
                .getTrendingList(
                    fetchFromRemote,
                    isRefresh,
                    ALL,
                    TRENDING_TIME,
                    mainUiState.value.trendingAllPage,
                    API_KEY
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { mediaList ->

                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if (isRefresh) {
                                    _mainUiState.update {
                                        it.copy(
                                            trendingAllList = shuffledMediaList.toList(),
                                            trendingAllPage = 1
                                        )
                                    }
                                } else {
                                    _mainUiState.update {
                                        it.copy(
                                            trendingAllList =
                                            mainUiState.value.trendingAllList + shuffledMediaList.toList(),
                                            trendingAllPage = mainUiState.value.trendingAllPage + 1
                                        )
                                    }
                                }
                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun createSpecialList(
        mediaList: List<Media>,
        isRefresh: Boolean = false
    ) {

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    specialList = emptyList()
                )
            }
        }

        if (mainUiState.value.specialList.size >= 7) {
            return
        }


        val shuffledMediaList = mediaList.take(7).toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    specialList = shuffledMediaList
                )
            }
        } else {
            _mainUiState.update {
                it.copy(
                    specialList = mainUiState.value.specialList + shuffledMediaList
                )
            }

        }

        for (item in mainUiState.value.specialList) {
            Timber.tag("special_list").d(item.title)
        }
    }

    private fun createTvSeriesList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    tvSeriesList = shuffledMediaList.toList()
                )
            }
        } else {
            _mainUiState.update {
                it.copy(
                    tvSeriesList = mainUiState.value.tvSeriesList + shuffledMediaList.toList()
                )
            }
        }
    }

    private fun createTopRatedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    topRatedAllList = shuffledMediaList.toList()
                )
            }
        } else {
            _mainUiState.update {
                it.copy(
                    topRatedAllList = mainUiState.value.topRatedAllList + shuffledMediaList.toList()
                )
            }
        }
    }

    private fun createRecommendedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean
    ) {

        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if (isRefresh) {
            _mainUiState.update {
                it.copy(
                    recommendedAllList = shuffledMediaList.toList()
                )
            }
        } else {
            _mainUiState.update {
                it.copy(
                    recommendedAllList = mainUiState.value.recommendedAllList + shuffledMediaList.toList()
                )
            }
        }

        createSpecialList(
            mediaList = mediaList,
            isRefresh = isRefresh
        )
    }

}






