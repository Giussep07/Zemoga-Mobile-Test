package com.giussepr.zemoga.core.di

import android.content.Context
import androidx.room.Room
import com.giussepr.zemoga.data.database.ZemogaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Provides
  @Singleton
  fun provideZemogaDatabase(@ApplicationContext context: Context): ZemogaDatabase {
    return Room.databaseBuilder(
      context,
      ZemogaDatabase::class.java,
      "zemoga_database"
    )
      .build()
  }

  @Provides
  fun providePostDao(zemogaDatabase: ZemogaDatabase) = zemogaDatabase.postDao()

  @Provides
  fun provideUserDao(zemogaDatabase: ZemogaDatabase) = zemogaDatabase.userDao()

  @Provides
  fun provideCommentDao(zemogaDatabase: ZemogaDatabase) = zemogaDatabase.commentDao()
}
