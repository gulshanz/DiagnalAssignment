package com.taksande.gulshan.diagnal.data.local.dto

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("name") var name: String,
    @SerializedName("poster-image") val posterImage: String,
) {
    fun getImagePath(): String {
        return "file:///android_asset/${posterImage}"
    }
}