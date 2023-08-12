package com.onecoder.kotlinx.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onecoder.kotlinx.DetailsMovieActivity
import com.onecoder.kotlinx.databinding.PopularMovieItemRowBinding
import com.onecoder.kotlinx.response.popular.Result
import com.onecoder.kotlinx.utils.Constants.POSTER_BASEURL



class MovieAdapter:RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var binding: PopularMovieItemRowBinding
    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        binding= PopularMovieItemRowBinding.inflate(inflater,parent,false)
        context=parent.context
        return ViewHolder()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ViewHolder:RecyclerView.ViewHolder(binding.root){

        fun bind(item: Result){
            binding.apply {
                val moviePosterURL=POSTER_BASEURL + item.posterPath

                Glide.with(movieImage).load(moviePosterURL).into(movieImage)

                movieImage.setOnClickListener {
                    val intent= Intent(context, DetailsMovieActivity::class.java)
                    intent.putExtra("id",item.id)
                    context.startActivity(intent)
                }
            }
        }

    }

    private val differCallBack=object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this,differCallBack)
}