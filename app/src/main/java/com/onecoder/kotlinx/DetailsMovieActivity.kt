package com.onecoder.kotlinx

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.onecoder.kotlinx.api.ApiClient
import com.onecoder.kotlinx.api.ApiService
import com.onecoder.kotlinx.databinding.ActivityDetailsMovieBinding
import com.onecoder.kotlinx.response.popular.DetailsMovieResponse
import com.onecoder.kotlinx.utils.Constants.POSTER_BASEURL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsMovieActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailsMovieBinding

    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val movieId = intent.getIntExtra("id", 1)

        binding.apply {
            prgBarMovies.visibility = View.VISIBLE

            val callDetailsMovieApi = api.getMovieDetails(movieId)

            callDetailsMovieApi.enqueue(object : Callback<DetailsMovieResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<DetailsMovieResponse>,
                    response: Response<DetailsMovieResponse>
                ) {
                    prgBarMovies.visibility = View.GONE
                    when (response.code()) {
                        in 200..299 -> {
                            response.body().let { itBody ->
                                val imagePosterURL = POSTER_BASEURL + itBody!!.posterPath
                                Glide.with(imgMovie).load(imagePosterURL).into(imgMovie)
                                Glide.with(imgMovieBack).load(imagePosterURL).into(imgMovieBack)

                                tvMovieTitle.text = itBody.title
                                tvMovieTagLine.text = itBody.tagline
                                tvMovieDateRelease.text = itBody.releaseDate
                                tvMovieRating.text = itBody.voteAverage.toString()
                                tvMovieGenres.text =
                                    itBody.genres[0].name + ", " + itBody.genres[1].name
                                tvMovieBudget.text = itBody.budget.toString()
                                tvMovieRevenue.text = itBody.revenue.toString()
                                tvMovieOverview.text = itBody.overview
                                companyName.text = itBody.productionCompanies[0].name

                                val companyImage =
                                    POSTER_BASEURL + itBody.productionCompanies[0].logoPath
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