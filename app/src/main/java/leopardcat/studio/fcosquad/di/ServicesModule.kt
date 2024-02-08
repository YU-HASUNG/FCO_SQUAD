package leopardcat.studio.fcosquad.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leopardcat.studio.fcosquad.manager.SharedPrefManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(
        sharedPrefManager: SharedPrefManager
    ): SharedPreferences = sharedPrefManager.getSharedPref()

}