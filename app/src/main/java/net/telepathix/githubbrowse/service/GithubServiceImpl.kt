package net.telepathix.githubbrowse.service

import net.telepathix.githubbrowse.service.models.Repository
import net.telepathix.githubbrowse.service.models.User
import net.telepathix.githubbrowse.service.models.UserList
import retrofit2.Response
import javax.inject.Inject

class GithubServiceImpl @Inject constructor(val githubApi: GithubApi) : GithubService {
    override suspend fun getUsers(searchValue: String, page: Int, pageSize: Int): ServiceResult<UserList> {
        if (searchValue.isEmpty()) {
            return ServiceResult(UserList(0, false, emptyList()), false)
        }
        val response = githubApi.getUsersList("$searchValue in:login", page, pageSize)
        return buildResult(response)
    }

    override suspend fun getUserInfo(username: String): ServiceResult<User> {
        val response = githubApi.getUserInfo(username)
        return buildResult(response)
    }

    override suspend fun getUserRepositories(username: String): ServiceResult<List<Repository>> {
        val response = githubApi.getRepositories(username)
        return buildResult(response)
    }

    private fun <T>buildResult(response: Response<T>): ServiceResult<T> {
        if (!response.isSuccessful || response.body() == null) {
            return ServiceResult(null, true)
        }
        return ServiceResult(response.body(), false)
    }
}