package com.taksande.gulshan.diagnal.data.local.dto

import com.google.gson.annotations.SerializedName

data class Page(
    @SerializedName("content-items") val contentItems: ContentItems,

    @SerializedName("page-num") val pageNum: String,

    @SerializedName("page-size") val pageSize: String,

    @SerializedName("title") val title: String,

    @SerializedName("total-content-items") val totalContentItems: String
)