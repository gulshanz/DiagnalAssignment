package com.taksande.gulshan.diagnal.data.local.dto

import com.google.gson.annotations.SerializedName

data class ContentItems(
    @SerializedName("content") val content: List<Content>
)