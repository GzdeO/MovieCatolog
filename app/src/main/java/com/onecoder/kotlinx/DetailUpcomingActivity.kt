package com.onecoder.kotlinx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.onecoder.kotlinx.api.ApiClient
import com.onecoder.kotlinx.api.ApiService
import com.onecoder.kotlinx.databinding.ActivityDetailUpcomingBinding
import com.onecoder.kotlinx.response.popular.DetailsMovieResponse
import com.onecoder.kotlinx.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class DetailUpcomingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailUpcomingBinding

    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailUpcomingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val upcomingId=intent.getIntExtra("id",1)
        binding.apply {
            prgBarMovies.visibility = View.VISIBLE
            val callDetailUpcomingApi=api.getMovieDetails(upcomingId)
            callDetailUpcomingApi.enqueue(object : Callback<DetailsMovieResponse>{
                override fun onResponse(
                    call: Call<DetailsMovieResponse>,
                    response: Response<DetailsMovieResponse>
                ) {
                    prgBarMovies.visibility = View.GONE
                    when(response.code()){
                        in 200..299 -> {
                            response.body().let {upcomingBody->
                                val imagePosterURL = Constants.POSTER_BASEURL + upcomingBody!!.posterPath
                                Glide.with(imgMovie).load(imagePosterURL).into(imgMovie)
                                Glide.with(imgMovieBack).load(imagePosterURL).into(imgMovieBack)

                                tvMovieTitle.text = upcomingBody.title
                                tvMovieTagLine.text = upcomingBody.tagline
                                tvMovieDateRelease.text = upcomingBody.releaseDate
                                tvMovieRating.text = upcomingBody.voteAverage.toString()
                                tvMovieGenres.text =
                                    upcomingBody.genres[0].name + ", " + upcomingBody.genres[1].name
                                tvMovieBudget.text = upcomingBody.budget.toString()
                                tvMovieRevenue.text = upcomingBody.revenue.toString()
                                tvMovieOverview.text = upcomingBody.overview
                                companyName.text = upcomingBody.productionCompanies[0].name

                                val companyImage =
                                    Constants.POSTER_BASEURL + upcomingBody.productionCompanies[0].logoPath
                                Glide.with(companyImg).load(companyImage).into(companyImg)

                            }
                        }
                        in 300..399 -> {
                            Log.d("Response code", "Redirection Message: ${response.code()}")
                        }

                        in 400..499 -> {
                            Log.d("Response code", "Client Error Responses: ${response.code()}")
                        }

                        in 500..599 -> {
                            Log.d("Response code", "Server Error Responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<DetailsMovieResponse>, t: Throwable) {
                    prgBarMovies.visibility = View.GONE
                    Log.e("onFailure", "Error: ${t.message}")
                }

            })
            backButton.setOnClickListener {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}