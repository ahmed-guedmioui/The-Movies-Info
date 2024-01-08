package com.ahmedapps.themovies.di

import android.app.Application
import androidx.room.Room
import com.ahmedapps.themovies.media_details.data.remote.api.ExtraDetailsApi
import com.ahmedapps.themovies.main.data.local.genres.GenresDatabase
import com.ahmedapps.themovies.main.data.local.media.MediaDatabase
import com.ahmedapps.themovies.main.data.remote.api.GenresApi
import com.ahmedapps.themovies.main.data.remote.api.MediaApi
import com.ahmedapps.themovies.main.data.remote.api.MediaApi.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideGenreDatabase(app: Application): GenresDatabase {
        return Room.databaseBuilder(
            app,
            GenresDatabase::class.java,
            "genresdb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMediaDatabase(app: Application): MediaDatabase {
        return Room.databaseBuilder(
            app,
            MediaDatabase::class.java,
            "mediadb.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMoviesApi() : MediaApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(MediaApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGenresApi() : GenresApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(GenresApi::class.java)
    }

    @Singleton
    @Provides
    fun provideExtraDetailsApi() : ExtraDetailsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(ExtraDetailsApi::class.java)
    }

}









