package com.iremsilayildirim.homework1.ui.detailpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iremsilayildirim.homework1.databinding.ItemMovieDetailBinding

class MovieDetailAdapter(private val details: List<Pair<String, String>>) :
    RecyclerView.Adapter<MovieDetailAdapter.MovieDetailViewHolder>() {

    inner class MovieDetailViewHolder(private val binding: ItemMovieDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: Pair<String, String>) {
            binding.detailKeyTextView.text = detail.first
            binding.detailValueTextView.text = detail.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder {
        val binding = ItemMovieDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
        holder.bind(details[position])
    }

    override fun getItemCount(): Int = details.size
}
