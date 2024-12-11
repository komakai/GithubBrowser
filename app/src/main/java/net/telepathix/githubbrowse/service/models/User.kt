package net.telepathix.githubbrowse.service.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Long,
    @SerializedName("login") var login: String,
    @SerializedName("name") var name: String?,
    @SerializedName("avatar_url") var avatarUrl: String,
    @SerializedName("followers") var followers: Int,
    @SerializedName("following") var following: Int
)