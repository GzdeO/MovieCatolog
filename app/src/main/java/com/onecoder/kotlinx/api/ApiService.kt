package com.onecoder.kotlinx.api

import com.onecoder.kotlinx.response.nowplaying.NowPlayingResponse
import com.onecoder.kotlinx.response.popular.DetailsMovieResponse
import com.onecoder.kotlinx.response.popular.MovieListResponse
import com.onecoder.kotlinx.response.upcoming.UpcomingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
//https://api.themoviedb.org/3/movie/popular?api_key=6a03b9b143e060d64d8e1e90f18f8807

    @GET("movie/popular")
    fun getPopularMovie(
        @Query("page") page:Int
    ): Call<MovieListResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id:Int
    ) : Call<DetailsMovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(
        @Query("page") page: Int
    ) : Call<NowPlayingResponse>

    @GET("movie/upcoming")
    fun getUpComingMovie(
        @Query("page") page: Int
    ) : Call<UpcomingResponse>


}