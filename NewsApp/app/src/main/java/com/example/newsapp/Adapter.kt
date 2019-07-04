package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.newsapp.model.Article
import kotlinx.android.synthetic.main.item_layout.view.*

class Adapter(private val context: Context, private val list : ArrayList<Article>):
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model : Article = list[position]
        val utils = Utils()

        val requestOptions = RequestOptions()
        requestOptions.placeholder(utils.getRandomColor())
        requestOptions.error(utils.getRandomColor())
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()


        Glide.with(context)
            .load(model.urlToImage)
            .apply(requestOptions)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                   return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }
            }).transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageView)

        holder.title.text = model.title
        holder.desc.text = model.description
        holder.source.text = model.source?.name
        holder.author.text =model.author
        holder.time.text = "\u2022 " + model.publishAt?.let { utils.DateFormat(it) }
        holder.publishedAt.text = utils.DateFormat(model.publishAt.toString())
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val title : TextView = view.title
        val desc : TextView = view.desc
        val author : TextView = view.author
        val publishedAt : TextView = view.publishedAt
        val source : TextView = view.source
        val time : TextView = view.time
        val imageView : ImageView = view.img
        val progressBar : ProgressBar = view.prograss_load_photo

    }
}