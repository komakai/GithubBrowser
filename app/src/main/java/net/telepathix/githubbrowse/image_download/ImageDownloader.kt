package net.telepathix.githubbrowse.image_download

import android.content.Context
import android.widget.ImageView

interface ImageDownloader {
    fun downloadImage(context: Context, url: String, targetView: ImageView)
}