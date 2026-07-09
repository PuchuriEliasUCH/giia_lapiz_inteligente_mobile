package com.giia.lapiz_inteligente.di

import com.giia.lapiz_inteligente.data.remote.ApiService
import com.giia.lapiz_inteligente.data.remote.BaseUrlHolder
import com.giia.lapiz_inteligente.data.datastore.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        baseUrlHolder: BaseUrlHolder,
        sessionManager: SessionManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val baseUrl = baseUrlHolder.get()
                val baseHttpUrl = baseUrl.toHttpUrlOrNull() ?: return@addInterceptor chain.proceed(original)

                val newUrl = original.url.newBuilder()
                    .scheme(baseHttpUrl.scheme)
                    .host(baseHttpUrl.host)
                    .port(baseHttpUrl.port)
                    .build()

                val newRequest = original.newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(newRequest)
            }
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val path = original.url.encodedPath
                val isPublicEndpoint = path == "/health" ||
                    path == "/auth/login" ||
                    path == "/auth/register"

                if (isPublicEndpoint || original.header("Authorization") != null) {
                    return@addInterceptor chain.proceed(original)
                }

                val token = runBlocking { sessionManager.token.first() }
                val authenticatedRequest = if (token.isNullOrBlank()) {
                    original
                } else {
                    original.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                }

                chain.proceed(authenticatedRequest)
            }
            .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://placeholder.local/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
