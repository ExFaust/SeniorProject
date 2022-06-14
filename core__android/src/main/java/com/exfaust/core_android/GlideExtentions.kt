package com.exfaust.core_android

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions

val Context.images get() = Images.with(this)
val Activity.images get() = Images.with(this)
val FragmentActivity.images get() = Images.with(this)
val View.images get() = Images.with(this)
val Fragment.images get() = Images.with(this)
val RecyclerView.ViewHolder.images get() = Images.with(itemView)

@GlideModule(glideName = "Images")
class ImagesModule : AppGlideModule() {
    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {
        builder.setLogLevel(Log.ERROR)
        builder.setDefaultTransitionOptions(
            Drawable::class.java,
            DrawableTransitionOptions.withCrossFade()
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}

@GlideExtension
object GlideExtensions {
    @GlideOption
    @JvmStatic
    fun roundCorners(options: BaseRequestOptions<*>, radiusPx: Int): BaseRequestOptions<*> =
        options.apply(RequestOptions().transform(RoundedCorners(radiusPx)))

    @GlideOption
    @JvmStatic
    fun centerCropWithRoundCorners(
        options: BaseRequestOptions<*>,
        radiusPx: Int
    ): BaseRequestOptions<*> =
        options.apply(
            RequestOptions().transform(
                CenterCrop(),
                RoundedCorners(radiusPx)
            )
        )
}