package com.capstoneproject.silimbah.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.silimbah.R
import com.capstoneproject.silimbah.model.DataCraft
import kotlinx.android.synthetic.main.item_craft.view.*

class CraftAdapter: RecyclerView.Adapter<CraftAdapter.ViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private var listData = ArrayList<DataCraft>()
    fun setData(items: ArrayList<DataCraft>){
        listData.clear()
        listData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(craft: DataCraft) {
            with(itemView) {
                Glide.with(itemView.context)
                        .load(craft.image_url)
                        .into(imageView)
                tv_title.text = craft.title

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(craft) }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_craft, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    interface OnItemClickCallback {
        fun onItemClicked(data: DataCraft)
    }
}