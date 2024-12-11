package net.telepathix.githubbrowse.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.telepathix.githubbrowse.service.GithubService
import net.telepathix.githubbrowse.service.models.User

class UsersPageSource(private val githubService: GithubService, private val searchTerm: String): PagingSource<Int, User>() {

    companion object{
        const val PAGE_SIZE = 50
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val nextPageNumber = params.key ?: 1
        val usersResult = githubService.getUsers(searchTerm, nextPageNumber, PAGE_SIZE)
        if (!usersResult.error) {
            val users = usersResult.data!!.items
            return LoadResult.Page(
                data = users,
                prevKey = null, // Only paging forward.
                nextKey = if (users.isNotEmpty()) nextPageNumber + 1 else null
            )
        } else {
            return LoadResult.Error(RuntimeException("Error"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}