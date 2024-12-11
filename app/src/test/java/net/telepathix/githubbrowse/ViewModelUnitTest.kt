@file:OptIn(ExperimentalCoroutinesApi::class)
package net.telepathix.githubbrowse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.telepathix.githubbrowse.ui.ServiceStatus
import net.telepathix.githubbrowse.ui.search.SearchUsersViewModel
import net.telepathix.githubbrowse.ui.user.UserViewModel
import net.telepathix.sharedtestcode.service.MockGithubService
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MainDispatcherRule(private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class ViewModelUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun testUsers() = runTest {
        val searchUsersViewModel = SearchUsersViewModel(MockGithubService())
        searchUsersViewModel.updateSearchTerm("koma")
        val users = searchUsersViewModel.users.asSnapshot()
        assert(users.size == 10)
        assert(users[0].login == "koma01")
        assert(users[1].login == "koma02")
        assert(users[2].login == "koma03")
        assert(users[3].login == "koma04")
        assert(users[4].login == "koma05")
        assert(users[5].login == "koma06")
        assert(users[6].login == "koma07")
        assert(users[7].login == "koma08")
        assert(users[8].login == "koma09")
        assert(users[9].login == "koma10")
    }

    @Test
    fun testRepositories() = runTest {
        val userViewModel = UserViewModel(MockGithubService())

        val repositoryValues = setup(userViewModel.repositoriesLiveData, backgroundScope, testScheduler)
        val userValues = setup(userViewModel.userLiveData, backgroundScope, testScheduler)
        val statusValues = setup(userViewModel.statusLiveData, backgroundScope, testScheduler)

        userViewModel.getDataForUser("koma01")
        assert(repositoryValues.size == 1)
        assert(repositoryValues[0].size == 4)
        assert(repositoryValues[0][0].name == "repo1")
        assert(repositoryValues[0][0].description == "Repo 1")
        assert(repositoryValues[0][0].language == "C++")
        assert(userValues.size == 1)
        assert(userValues[0].name == "Koma Neko")
        assert(userValues[0].followers == 12)
        assert(userValues[0].following == 5)
        assert(statusValues == listOf(ServiceStatus.None, ServiceStatus.InProgress, ServiceStatus.SuccessWithData))
    }

    fun <T>setup(liveData: LiveData<T>, scope: CoroutineScope, testScheduler: TestCoroutineScheduler): MutableList<T> {
        val list = mutableListOf<T>()
        scope.launch(UnconfinedTestDispatcher(testScheduler)) {
            liveData.asFlow().toList(list)
        }
        return list
    }
}