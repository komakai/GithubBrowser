package net.telepathix.githubbrowse.service.models

data class UserList(
    val totalCount: Long,
    val incompleteResults: Boolean,
    val items: List<User>
)