package com.taksande.gulshan.diagnal.paging

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.taksande.gulshan.diagnal.R
import com.taksande.gulshan.diagnal.data.local.dto.Content
import com.taksande.gulshan.diagnal.databinding.ItemBookBinding

class BooksAdapter : PagingDataAdapter<Content, BooksAdapter.BooksViewHolder>(BooksDiffCallBack()) {

    inner class BooksViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Content) {
            try {
                Glide.with(this.itemView.context).load(book.getImagePath())
                    .error(R.drawable.baseline_broken_image_24)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imgBook)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.txtTitle.text = book.name
        }
    }

    class BooksDiffCallBack : DiffUtil.ItemCallback<Content>() {
        override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem.name == newItem.name && oldItem.posterImage == newItem.posterImage
        }

        override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem.name == newItem.name
        }

        override fun getChangePayload(oldItem: Content, newItem: Content): Any? {
            if (oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }

    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
        }
    }

    override fun onBindViewHolder(
        holder: BooksViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val newItem = payloads[0] as Content
            holder.bind(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookBinding.inflate(inflater)
        return BooksViewHolder(binding)
    }
}


class ItemMarginDecoration(private val marginHorizontal: Int, private val marginVertical: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.apply {
            left = marginHorizontal
            right = marginHorizontal
            top = marginVertical
            bottom = marginVertical
        }
    }
}