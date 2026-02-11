package net.telepathix.githubbrowse.service.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.telepathix.githubbrowse.BuildConfig
import net.telepathix.githubbrowse.service.GithubApi
import net.telepathix.githubbrowse.service.GithubService
import net.telepathix.githubbrowse.service.GithubServiceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val TIME_OUT: Long = 20

@Module
@InstallIn(SingletonComponent::class)
class GithubServiceModule {
    @Provides
    @Singleton
    fun provideGithubService(githubApi: GithubApi): GithubService = GithubServiceImpl(githubApi)
}

@Module
@InstallIn(SingletonComponent::class)
class DataModules {

    @Provides
    @Singleton
    fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setStrictness(Strictness.LENIENT)
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.GITHUB_PAT.isNotEmpty()) {
                    addInterceptor { chain ->
                        val requestBuilder = chain.request().newBuilder()
                        requestBuilder.addHeader(
                            "Authorization",
                            "Bearer ${BuildConfig.GITHUB_PAT}"
                        )
                        chain.proceed(requestBuilder.build())
                    }
                }
                if (BuildConfig.DEBUG) {
                    addInterceptor(httpLoggingInterceptor)
                }
            }
            .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("GithubBrowser").i(message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}
