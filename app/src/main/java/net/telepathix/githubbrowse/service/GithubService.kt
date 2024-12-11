package net.telepathix.githubbrowse.service

import net.telepathix.githubbrowse.service.models.Repository
import net.telepathix.githubbrowse.service.models.User
import net.telepathix.githubbrowse.service.models.UserList

interface GithubService {
    suspend fun getUsers(searchValue: String, page: Int, pageSize: Int): ServiceResult<UserList>

    suspend fun getUserInfo(username: String): ServiceResult<User>

    suspend fun getUserRepositories(username: String): ServiceResult<List<Repository>>
}