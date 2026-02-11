package net.telepathix.githubbrowse.service.models

import com.google.gson.annotations.SerializedName

data class Repository (
    val id: Long,
    val name: String,
    val fork: Boolean,
    @SerializedName("stargazers_count") val starCount: Int,
    val description: String?,
    val language: String,
    val htmlUrl: String
)
