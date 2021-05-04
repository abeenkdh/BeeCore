package com.beestudio.beecore

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class BeeImage {
    fun load(block: LoadImageBuilder.() -> Unit){
        LoadImageBuilder().apply(block).build()
    }

    fun clear(block: ClearImageBuilder.() -> Unit){
        ClearImageBuilder().apply(block).build()
    }
}

class ClearImageBuilder {
    lateinit var imageView: ImageView
    fun build() {
        Glide.with(imageView).clear(imageView)
    }
}

class LoadImageBuilder {
    lateinit var imageView: ImageView
    lateinit var url: String
    var placeholder: Int? = R.drawable.placeholder
    var listener: ((Bitmap) -> Unit?)? = null
    fun build() {
        if(listener != null){
            Glide.with(imageView)
                .asBitmap()
                .load(url)
                .placeholder(placeholder!!)
                .transition(BitmapTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Bitmap>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        listener!!.invoke(resource!!)
                        imageView.setImageBitmap(resource)
                        return true
                    }

                })
                .submit()
        } else {
            Glide.with(imageView)
                .asBitmap()
                .load(url)
                .placeholder(placeholder!!)
                .transition(BitmapTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    }
}