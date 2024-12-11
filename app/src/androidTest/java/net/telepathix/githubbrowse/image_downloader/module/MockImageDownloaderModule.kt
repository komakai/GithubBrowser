package net.telepathix.githubbrowse.image_downloader.module

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import net.telepathix.githubbrowse.image_download.ImageDownloader
import net.telepathix.githubbrowse.image_download.module.ImageDownloaderModule
import net.telepathix.githubbrowse.image_downloader.MockImageDownloader
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ImageDownloaderModule::class]
)
abstract class MockImageDownloaderModule {
    @Singleton
    @Binds
    abstract fun bindImageDownloader(mockImageDownloader: MockImageDownloader): ImageDownloader
}