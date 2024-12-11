package net.telepathix.githubbrowse.service.models

import com.google.gson.annotations.SerializedName

data class Repository (
    @SerializedName("id") var id: Long,
    @SerializedName("name") var name: String,
    @SerializedName("fork") var fork: Boolean,
    @SerializedName("stargazers_count") var starCount: Int,
    @SerializedName("description") var description: String?,
    @SerializedName("language") var language: String,
    @SerializedName("html_url") var htmlUrl: String
)
