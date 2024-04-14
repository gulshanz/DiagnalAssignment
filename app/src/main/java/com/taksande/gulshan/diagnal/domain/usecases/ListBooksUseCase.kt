package com.taksande.gulshan.diagnal.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.taksande.gulshan.diagnal.data.local.dto.Content
import com.taksande.gulshan.diagnal.paging.BooksDataSource
import com.taksande.gulshan.diagnal.utils.Constants.PAGE_SIZE
import javax.inject.Inject

class ListBooksUseCase @Inject constructor(
    private val pagingSource: BooksDataSource
) {
    operator fun invoke(): LiveData<PagingData<Content>> {
        return Pager(config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ), pagingSourceFactory = {
            pagingSource
        }).liveData
    }
}