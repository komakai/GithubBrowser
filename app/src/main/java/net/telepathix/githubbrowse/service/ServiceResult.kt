package net.telepathix.githubbrowse.service

class ServiceResult<T>(
    val data: T? = null,
    val error: Boolean
)