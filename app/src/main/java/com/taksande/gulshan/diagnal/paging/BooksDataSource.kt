package com.taksande.gulshan.diagnal.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taksande.gulshan.diagnal.data.local.dto.BooksResponse
import com.taksande.gulshan.diagnal.data.local.dto.Content
import com.taksande.gulshan.diagnal.domain.data.Resource
import com.taksande.gulshan.diagnal.domain.repository.BooksRepository
import com.taksande.gulshan.diagnal.utils.Constants.STARTING_PAGE_INDEX
import javax.inject.Inject

class BooksDataSource @Inject constructor(private val booksRepository: BooksRepository) :
    PagingSource<Int, Content>() {


    override fun getRefreshKey(state: PagingState<Int, Content>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Content> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            when (val response = booksRepository.fetchBooks(position)) {
                is Resource.Error -> LoadResult.Error(Exception(response.toString()))
                is Resource.Success -> {
                    LoadResult.Page(
                        data = response.data!!.page!!.contentItems.content,
                        prevKey = if (position == 1) null else (position - 1),
                        nextKey = getNextKey(response.data, position)
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(Exception("Something went wrong!"))
        }
    }

    private fun getNextKey(response: BooksResponse?, position: Int): Int? {
        return try {
            if (response!!.page!!.contentItems.content.isEmpty()) null
            else position + 1
        } catch (e: Exception) {
            null
        }

    }


}