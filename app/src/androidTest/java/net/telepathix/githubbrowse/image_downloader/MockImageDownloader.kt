package net.telepathix.githubbrowse.image_downloader

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import net.telepathix.githubbrowse.R
import net.telepathix.githubbrowse.image_download.ImageDownloader
import javax.inject.Inject

class MockImageDownloader @Inject constructor(): ImageDownloader {
    override fun downloadImage(context: Context, url: String, targetView: ImageView) {
        val mockImage = ContextCompat.getDrawable(context, R.drawable.mock_avatar)
        targetView.setImageDrawable(mockImage)
    }
}