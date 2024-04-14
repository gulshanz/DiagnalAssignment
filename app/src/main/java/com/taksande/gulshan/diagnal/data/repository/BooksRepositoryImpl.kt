package com.taksande.gulshan.diagnal.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taksande.gulshan.diagnal.data.local.dto.BooksResponse
import com.taksande.gulshan.diagnal.domain.data.Resource
import com.taksande.gulshan.diagnal.domain.repository.BooksRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject


class BooksRepositoryImpl @Inject constructor(@ApplicationContext val context: Context) :
    BooksRepository {
    override suspend fun fetchBooks(pageNum: Int): Resource<BooksResponse> {
        val endPoint = "CONTENTLISTINGPAGE-PAGE${pageNum}.json"
        return try {
            withContext(Dispatchers.IO) {
                val inputStream: InputStream = context.assets.open(endPoint)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                val json = String(buffer, charset("UTF-8"))
                val gson = Gson()
                val type = object : TypeToken<BooksResponse>() {}.type

                val data = gson.fromJson(json, type) as BooksResponse
                Resource.Success(data)
            }

        } catch (e: Exception) {
            Resource.Error("Something went wrong!")
        }
    }

}