package net.telepathix.githubbrowse.image_download.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.telepathix.githubbrowse.image_download.GlideImageDownloaderImpl
import net.telepathix.githubbrowse.image_download.ImageDownloader
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ImageDownloaderModule {
    @Provides
    @Singleton
    fun provideImageDownloader(): ImageDownloader = GlideImageDownloaderImpl()
}