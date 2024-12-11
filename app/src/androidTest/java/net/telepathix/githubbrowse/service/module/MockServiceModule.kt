package net.telepathix.githubbrowse.service.module

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import net.telepathix.githubbrowse.service.GithubService
import net.telepathix.githubbrowse.service.MockGithubServiceInjectable
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GithubServiceModule::class]
)
abstract class MockServiceModule {
    @Singleton
    @Binds
    abstract fun bindGithubService(mockGithubService: MockGithubServiceInjectable): GithubService
}