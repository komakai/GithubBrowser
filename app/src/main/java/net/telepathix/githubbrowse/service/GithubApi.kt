package net.telepathix.githubbrowse.service
import net.telepathix.githubbrowse.service.models.Repository
import net.telepathix.githubbrowse.service.models.User
import net.telepathix.githubbrowse.service.models.UserList
import retrofit2.Response
import retrofit2.http.*

interface GithubApi {
    @GET("search/users")
    suspend fun getUsersList(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Response<UserList>

    @GET("users/{username}")
    suspend fun getUserInfo(
        @Path("username") username: String
    ): Response<User>

    @GET("users/{username}/repos")
    suspend fun getRepositories(
        @Path("username") username: String
    ): Response<List<Repository>>
}