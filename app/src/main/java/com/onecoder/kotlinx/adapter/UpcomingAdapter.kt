package com.onecoder.kotlinx.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onecoder.kotlinx.DetailUpcomingActivity
import com.onecoder.kotlinx.databinding.UpcomingRowBinding
import com.onecoder.kotlinx.response.upcoming.Result
import com.onecoder.kotlinx.utils.Constants.POSTER_BASEURL


class UpcomingAdapter : RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder>() {

    private lateinit var binding: UpcomingRowBinding
    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        binding= UpcomingRowBinding.inflate(inflater,parent,false)
        context=parent.context
        return UpcomingViewHolder()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class UpcomingViewHolder :RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result){
            binding.apply {
                val posterUrl=POSTER_BASEURL + item.posterPath
                Glide.with(upcommingImage).load(posterUrl).into(upcommingImage)
                movieTitle.text=item.title
                tvStarRating.text=item.voteAverage.toString()
                upcommingImage.setOnClickListener {
                    val intent= Intent(context,DetailUpcomingActivity:: class.java)
                    intent.putExtra("id",item.id)
                    context.startActivity(intent)
                }
            }
        }

    }

    private val differCallback=object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem==newItem
        }

    }

    val differ= AsyncListDiffer(this,differCallback)

}