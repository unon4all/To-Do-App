package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context = context, klass = TodoDatabase::class.java, name = DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideDao(database: TodoDatabase) = database.toDoDao()

}