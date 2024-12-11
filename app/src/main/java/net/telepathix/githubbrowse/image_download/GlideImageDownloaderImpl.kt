package net.telepathix.githubbrowse.image_download

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class GlideImageDownloaderImpl: ImageDownloader {
    override fun downloadImage(context: Context, url: String, targetView: ImageView) {
        if (url.isNotEmpty()) {
            Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(targetView)
        }
    }
}