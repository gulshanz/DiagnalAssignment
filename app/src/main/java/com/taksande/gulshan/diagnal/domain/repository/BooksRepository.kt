package com.taksande.gulshan.diagnal.domain.repository

import com.taksande.gulshan.diagnal.data.local.dto.BooksResponse
import com.taksande.gulshan.diagnal.domain.data.Resource

interface BooksRepository {
    suspend fun fetchBooks(pageNum: Int): Resource<BooksResponse>
}