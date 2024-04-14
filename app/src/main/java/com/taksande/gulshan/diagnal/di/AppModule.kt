package com.taksande.gulshan.diagnal.di

import android.content.Context
import com.taksande.gulshan.diagnal.data.repository.BooksRepositoryImpl
import com.taksande.gulshan.diagnal.paging.BooksDataSource
import com.taksande.gulshan.diagnal.domain.repository.BooksRepository
import com.taksande.gulshan.diagnal.domain.usecases.ListBooksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun getBooksRepository(@ApplicationContext context: Context): BooksRepository {
        return BooksRepositoryImpl(context)
    }

    @Provides
    fun getBooksDataSource(booksRepository: BooksRepository): BooksDataSource {
        return BooksDataSource(booksRepository)
    }


    @Provides
    fun getBooksUseCase(
        booksDataSource: BooksDataSource
    ): ListBooksUseCase {
        return ListBooksUseCase(booksDataSource)
    }
}