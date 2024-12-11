package net.telepathix.githubbrowse.service.models

import com.google.gson.annotations.SerializedName

data class UserList(
    @SerializedName("total_count") var totalCount: Long,
    @SerializedName("incomplete_results") var incompleteResults: Boolean,
    @SerializedName("items") var items: List<User>
)