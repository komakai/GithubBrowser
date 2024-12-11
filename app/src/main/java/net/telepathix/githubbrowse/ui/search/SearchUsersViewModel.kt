package net.telepathix.githubbrowse.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import net.telepathix.githubbrowse.ui.ServiceStatus
import net.telepathix.githubbrowse.service.GithubService
import net.telepathix.githubbrowse.service.models.User
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(val githubService: GithubService) : ViewModel() {

    var usersLiveData: LiveData<PagingData<User>>? = null
    var searchTermLiveData: MutableLiveData<String> = MutableLiveData()
    var statusLiveData: MutableLiveData<ServiceStatus> = MutableLiveData(ServiceStatus.None)

    private val config = PagingConfig(
        pageSize = UsersPageSource.PAGE_SIZE,
        enablePlaceholders = true,
        initialLoadSize = UsersPageSource.PAGE_SIZE,
        prefetchDistance = 3
    )

    private fun usersPageSourceFlow(user: String) = Pager(config) { UsersPageSource(githubService, user) }.flow

    @OptIn(ExperimentalCoroutinesApi::class)
    val users = searchTermLiveData
        .asFlow()
        .flatMapLatest { usersPageSourceFlow(it) }
        .cachedIn(viewModelScope)

    fun updateSearchTerm(searchTerm: String) {
        searchTermLiveData.postValue(searchTerm)
    }
}
