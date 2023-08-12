package com.onecoder.kotlinx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.onecoder.kotlinx.adapter.MovieAdapter
import com.onecoder.kotlinx.adapter.UpcomingAdapter
import com.onecoder.kotlinx.api.ApiClient
import com.onecoder.kotlinx.api.ApiService
import com.onecoder.kotlinx.databinding.ActivityMainBinding
import com.onecoder.kotlinx.response.nowplaying.NowPlayingResponse
import com.onecoder.kotlinx.response.popular.MovieListResponse
import com.onecoder.kotlinx.response.upcoming.UpcomingResponse
import com.onecoder.kotlinx.utils.Constants.POSTER_BASEURL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private val movieAdapter by lazy { MovieAdapter() }

    private val upcomingAdapter by lazy{UpcomingAdapter()}


    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.apply {
            prgBar.visibility= View.VISIBLE

            val callMovieApi=api.getPopularMovie(1)

            callMovieApi.enqueue(object : Callback<MovieListResponse>{
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    prgBar.visibility=View.GONE
                    when(response.code()){
                        in 200..299->{
                            response.body().let {itBody->
                                itBody?.results.let {itData->
                                    if (itData!!.isNotEmpty()){
                                        movieAdapter.differ.submitList(itData)
                                        carouselRecyclerView.apply {
                                            layoutManager= LinearLayoutManager(this@MainActivity)
                                            adapter=movieAdapter
                                            carouselRecyclerView.set3DItem(true)
                                            carouselRecyclerView.setAlpha(true)
                                            carouselRecyclerView.setInfinite(true)

                                        }


                                    }
                                }
                            }
                        }

                        in 300..399->{
                            Log.d("Response code", "Redirection Message: ${response.code()}")
                        }
                        in 400..499->{
                            Log.d("Response code", "Client Error Responses: ${response.code()}")
                        }
                        in 500..599->{
                            Log.d("Response code", "Server Error Responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    binding.prgBar.visibility=View.GONE
                    Log.e("onFailure", "Error: ${t.message}")
                }

            })
        }

        binding.apply {
            prgBar.visibility= View.VISIBLE
            val callNowPlayingApi=api.getNowPlayingMovie(1)
            callNowPlayingApi.enqueue(object : Callback<NowPlayingResponse>{
                override fun onResponse(
                    call: Call<NowPlayingResponse>,
                    response: Response<NowPlayingResponse>
                ) {
                    prgBar.visibility=View.GONE
                    when(response.code()){
                        in 200..299->{
                            val imageList = ArrayList<SlideModel>()
                            response.body().let {itNow->
                            itNow?.results.let {itLast->
                                if (itLast!!.isNotEmpty()){
                                    itLast.forEach {result ->
                                        val posterURL = POSTER_BASEURL + result.posterPath
                                        imageList.add(SlideModel(posterURL))

                                    }
                                    imageSlider.setImageList(imageList, ScaleTypes.FIT)
                                }
                            }

                            }
                        }
                        in 300..399->{
                            Log.d("Response code", "Redirection Message: ${response.code()}")
                        }
                        in 400..499->{
                            Log.d("Response code", "Client Error Responses: ${response.code()}")
                        }
                        in 500..599->{
                            Log.d("Response code", "Server Error Responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<NowPlayingResponse>, t: Throwable) {
                    binding.prgBar.visibility=View.GONE
                    Log.e("onFailure", "Error: ${t.message}")
                }

            })


        }

        binding.apply {
            prgBar.visibility=View.VISIBLE
            val callUpcomingApi=api.getUpComingMovie(1)
            callUpcomingApi.enqueue(object : Callback<UpcomingResponse>{
                override fun onResponse(
                    call: Call<UpcomingResponse>,
                    response: Response<UpcomingResponse>
                ) {
                    prgBar.visibility=View.GONE
                    when(response.code()){
                        in 200..299->{
                            response.body().let {upcomingBody->
                                upcomingBody?.results.let {upcomingData->
                                    if(upcomingData!!.isNotEmpty()){
                                        upcomingAdapter.differ.submitList(upcomingData)
                                        upcommingCarousel.apply {
                                            layoutManager= LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                                            adapter=upcomingAdapter
                                        }
                                    }

                                }

                            }
                        }

                        in 300..399->{
                            Log.d("Response code", "Redirection Message: ${response.code()}")
                        }
                        in 400..499->{
                            Log.d("Response code", "Client Error Responses: ${response.code()}")
                        }
                        in 500..599->{
                            Log.d("Response code", "Server Error Responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<UpcomingResponse>, t: Throwable) {
                    binding.prgBar.visibility=View.GONE
                    Log.e("onFailure", "Error: ${t.message}")
                }

            })
        }

    }
}


