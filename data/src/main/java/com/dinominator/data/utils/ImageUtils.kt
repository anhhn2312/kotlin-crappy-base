package com.dinominator.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Andy Ha on 6/21/2019.
 */

object ImageUtils {

    fun loadImage(imageView: ImageView, imageUrl: String?) {
        if (imageUrl == null) return
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, resId: Int) {
        Glide.with(imageView.context)
            .load(resId)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, imageUrl: String?, placeHolder: Int) {
        if (imageUrl == null) return
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(RequestOptions().placeholder(placeHolder))
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, imageUrl: String?, placeHolder: Int, fallbackId: Int) {
        if (imageUrl == null) return
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(RequestOptions().placeholder(placeHolder).error(fallbackId))
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, imageUrl: String?, fallbackImageUrl: String) {
        if (imageUrl == null) return
        Glide.with(imageView.context)
            .load(imageUrl)
            .error(Glide.with(imageView.context).load(fallbackImageUrl))
            .into(imageView)
    }

    fun createBitmapFromUrl(context: Context, url: String, width: Int, height: Int): Bitmap? {
        try {
            return Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(width, height)
                .get()
        } catch (e: Exception) {
            return null
        }
    }

    fun createBitmapFromUrl(context: Context, url: String): Bitmap? {
        try {
            return Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: Exception) {
            return null
        }
    }
}