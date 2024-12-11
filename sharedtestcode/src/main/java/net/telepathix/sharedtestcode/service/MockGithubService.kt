package net.telepathix.sharedtestcode.service

import net.telepathix.githubbrowse.service.GithubService
import net.telepathix.githubbrowse.service.ServiceResult
import net.telepathix.githubbrowse.service.models.Repository
import net.telepathix.githubbrowse.service.models.User
import net.telepathix.githubbrowse.service.models.UserList
import kotlin.math.min

open class MockGithubService: GithubService {

    private val komaUsers = listOf(
        makeUser(0, "koma01"),
        makeUser(1, "koma02"),
        makeUser(2, "koma03"),
        makeUser(3, "koma04"),
        makeUser(4, "koma05"),
        makeUser(5, "koma06"),
        makeUser(6, "koma07"),
        makeUser(7, "koma08"),
        makeUser(8, "koma09"),
        makeUser(9, "koma10")
    )
    private val repos = listOf(
        Repository(1, "repo1", false, 0, "Repo 1", "C++", ""),
        Repository(2, "repo2", false, 0, "Repo 2", "Kotlin", ""),
        Repository(3, "repo3", false, 0, "Repo 3", "Go", ""),
        Repository(4, "repo4", true, 0, "Repo 4", "Swift", ""),
        Repository(5, "repo5", false, 0, "Repo 5", "Objective-C", "")
    )

    override suspend fun getUsers(searchValue: String, page: Int, pageSize: Int): ServiceResult<UserList> {
        return when(searchValue) {
            "koma" -> {
                val startIndex = (page - 1) * pageSize
                val endIndex = (page) * pageSize
                if (startIndex < komaUsers.size)
                    ServiceResult(UserList(komaUsers.size.toLong(), false, komaUsers.subList(startIndex, min(endIndex, komaUsers.size))), false)
                else
                    ServiceResult(UserList(komaUsers.size.toLong(), false, emptyList()), false)
            }
            "simulate_error" -> ServiceResult(null, true)
            else -> ServiceResult(UserList(0, false, emptyList()), false)
        }
    }

    override suspend fun getUserInfo(username: String): ServiceResult<User> {
        return when (username) {
            "koma01" -> ServiceResult(User(0, "koma01", "Koma Neko", "", 12, 5), false)
            "koma02" -> ServiceResult(User(1, "koma02", "Koma Inu", "", 12, 5), false)
            else -> ServiceResult(User(99999, username, "", "", 0, 0), false)
        }
    }

    override suspend fun getUserRepositories(username: String): ServiceResult<List<Repository>> {
        return when (username) {
            "koma01" -> ServiceResult(repos, false)
            else -> ServiceResult(emptyList(), false)
        }
    }

}

fun makeUser(id: Long, userName: String) = User(id, userName, null, "", 0, 0)