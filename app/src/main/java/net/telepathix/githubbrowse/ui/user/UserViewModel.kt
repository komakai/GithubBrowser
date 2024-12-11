package net.telepathix.githubbrowse.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.telepathix.githubbrowse.ui.ServiceStatus
import net.telepathix.githubbrowse.service.GithubService
import net.telepathix.githubbrowse.service.models.Repository
import net.telepathix.githubbrowse.service.models.User
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val githubService: GithubService) : ViewModel() {

    var userLiveData: MutableLiveData<User> = MutableLiveData()
    var repositoriesLiveData: MutableLiveData<List<Repository>> = MutableLiveData()
    var statusLiveData: MutableLiveData<ServiceStatus> = MutableLiveData(ServiceStatus.None)

    fun getDataForUser(username: String) {
        viewModelScope.launch {
            statusLiveData.postValue(ServiceStatus.InProgress)
            val userInfoResult = githubService.getUserInfo(username)
            if (!userInfoResult.error) {
                userLiveData.postValue(userInfoResult.data!!)
            } else {
                userLiveData.postValue(User(0, username, null, "", 0, 0))
            }
            val repositoriesResult = githubService.getUserRepositories(username)
            if (!repositoriesResult.error) {
                val repositories = repositoriesResult.data!!.filter { !it.fork }
                repositoriesLiveData.postValue(repositories)
                statusLiveData.postValue(if (repositories.isNotEmpty()) ServiceStatus.SuccessWithData else ServiceStatus.SuccessEmpty)
            } else {
                statusLiveData.postValue(ServiceStatus.Error)
            }
        }
    }
}
