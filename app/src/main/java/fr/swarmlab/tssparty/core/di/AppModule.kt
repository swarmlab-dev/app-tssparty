package fr.swarmlab.tssparty.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.swarmlab.tssparty.core.datastore.AppPreferences
import fr.swarmlab.tssparty.core.datastore.AppPreferencesImpl


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferencesImpl(context)
    }
}