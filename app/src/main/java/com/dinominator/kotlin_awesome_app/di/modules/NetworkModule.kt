package com.dinominator.kotlin_awesome_app.di.modules

import com.dinominator.data.storage.AppSharedPreference
import com.dinominator.domain.HttpLoggingInterceptor
import com.dinominator.domain.repository.services.AuthenticationService
import com.dinominator.kotlin_awesome_app.BuildConfig
import com.dinominator.kotlin_awesome_app.platform.http.AutoAuthenticator
import com.dinominator.kotlin_awesome_app.platform.http.TokenInterceptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tech.linjiang.pandora.Pandora
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Modifier
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create()


    @Singleton
    @Provides
    fun provideTokenInterceptor(prefs: AppSharedPreference) = TokenInterceptor(prefs)

    @Singleton
    @Provides
    fun provideAutoAuthenticator() = AutoAuthenticator()

    @Singleton
    @Provides
    fun provideDatabaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideHttpClient(
        tokenInterceptor: TokenInterceptor,
        autoRefresh: AutoAuthenticator
    ): OkHttpClient {
        kotlin.runCatching {
            return OkHttpClient
                .Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(Pandora.get().interceptor)
                .addInterceptor(HttpLoggingInterceptor(debug = BuildConfig.DEBUG))
                .authenticator(autoRefresh)
                .build()
        }.getOrElse {
            Timber.tag("NetworkModule").d("has no ssl certificate")
            return OkHttpClient
                .Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(Pandora.get().interceptor)
                .addInterceptor(HttpLoggingInterceptor(debug = BuildConfig.DEBUG))
                .authenticator(autoRefresh)
                .build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ResponseConverter(gson))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gson: Gson, okHttpClient: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ResponseConverter(gson))
        .client(okHttpClient)


    @Singleton
    @Provides
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService =
        retrofit.create(AuthenticationService::class.java)
}

private class ResponseConverter(
    private val gson: Gson,
    private val creator: GsonConverterFactory = GsonConverterFactory.create(gson)
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return try {
            if (type === String::class.java) {
                StringResponseConverter()
            } else {
                creator.responseBodyConverter(type, annotations, retrofit)
            }
        } catch (ignored: OutOfMemoryError) {
            null
        }
    }

    override fun requestBodyConverter(
        type: Type, parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return creator.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    private class StringResponseConverter : Converter<ResponseBody, String> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {
            return value.string()
        }
    }
}