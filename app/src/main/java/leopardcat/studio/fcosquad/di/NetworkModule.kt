package leopardcat.studio.fcosquad.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leopardcat.studio.fcosquad.data.source.FcoDivisionService
import leopardcat.studio.fcosquad.data.source.FcoIdService
import leopardcat.studio.fcosquad.data.source.FcoMatchHistoryService
import leopardcat.studio.fcosquad.data.source.FcoMatchService
import leopardcat.studio.fcosquad.data.source.FcoMaxDivisionService
import leopardcat.studio.fcosquad.data.source.FcoPositionService
import leopardcat.studio.fcosquad.data.source.FcoSeasonService
import leopardcat.studio.fcosquad.data.source.FcoSpidService
import leopardcat.studio.fcosquad.data.source.FcoUserInfoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun apiKey(): String {
        return "live_~~~"
    }

    //TODO 로그 안찍히게
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    //계정 ouid 조회
    @Provides
    @Singleton
    fun provideFcoIdService(
        retrofit: Retrofit
    ): FcoIdService {
        return retrofit.create(FcoIdService::class.java)
    }

    //계정 정보 조회
    @Provides
    @Singleton
    fun provideFcoUserInfoService(
        retrofit: Retrofit
    ): FcoUserInfoService {
        return retrofit.create(FcoUserInfoService::class.java)
    }

    //역대 최고 등급 조회
    @Provides
    @Singleton
    fun provideFcoMaxDivisionService(
        retrofit: Retrofit
    ): FcoMaxDivisionService {
        return retrofit.create(FcoMaxDivisionService::class.java)
    }

    //유저 매치 기록 조회
    @Provides
    @Singleton
    fun provideFcoMatchHistoryService(
        retrofit: Retrofit
    ): FcoMatchHistoryService {
        return retrofit.create(FcoMatchHistoryService::class.java)
    }

    //매치 상세 기록 조회
    @Provides
    @Singleton
    fun provideFcoMatchService(
        retrofit: Retrofit
    ): FcoMatchService {
        return retrofit.create(FcoMatchService::class.java)
    }

    //선수 목록 조회
    @Provides
    @Singleton
    fun provideFcoSpidService(
        retrofit: Retrofit
    ): FcoSpidService {
        return retrofit.create(FcoSpidService::class.java)
    }

    //시즌 목록 조회
    @Provides
    @Singleton
    fun provideFcoSeasonService(
        retrofit: Retrofit
    ): FcoSeasonService {
        return retrofit.create(FcoSeasonService::class.java)
    }

    //포지션 목록 조회
    @Provides
    @Singleton
    fun provideFcoPositionService(
        retrofit: Retrofit
    ): FcoPositionService {
        return retrofit.create(FcoPositionService::class.java)
    }

    //등급식별자 목록 조회
    @Provides
    @Singleton
    fun provideFcoDivisionService(
        retrofit: Retrofit
    ): FcoDivisionService {
        return retrofit.create(FcoDivisionService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://open.api.nexon.com/"
    }
}