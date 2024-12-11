package net.telepathix.githubbrowse.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.telepathix.githubbrowse.ui.ServiceStatus
import net.telepathix.githubbrowse.R
import net.telepathix.githubbrowse.ui.user.UserFragment.Companion.ARG_USERID
import net.telepathix.githubbrowse.databinding.SearchUsersFragmentBinding
import net.telepathix.githubbrowse.image_download.ImageDownloader
import net.telepathix.githubbrowse.ui.handleServiceStatus
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class SearchUsersFragment : Fragment() {

    private val searchUsersViewModel: SearchUsersViewModel by viewModels()
    private var _binding: SearchUsersFragmentBinding? = null
    //onCreateViewへの呼び出しとonDestroyViewへの呼び出しの間飲み、利用できます
    private val binding get() = _binding!!
    @Inject lateinit var imageDownloader: ImageDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SearchUsersFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateSearchTerm()
                hideIme()
                true
            } else {
                false
            }
        }
        binding.searchBox.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateSearchTerm()
                hideIme()
                true
            } else {
                false
            }
        }
        initAdapter()
        handleServiceStatus(binding.userList, binding.loadingIndicator, binding.message, R.string.no_user,
            R.string.error_message, searchUsersViewModel.statusLiveData, viewLifecycleOwner)
        return view
    }

    private fun updateSearchTerm() {
        binding.searchBox.text?.trim().toString().let {
            if (binding.userList.adapter?.itemCount == 0) {
                searchUsersViewModel.statusLiveData.postValue(ServiceStatus.InProgress)
            }
            searchUsersViewModel.updateSearchTerm(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideIme() {
        activity?.currentFocus?.let { view ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun initAdapter() {
        val usersAdapter = UsersAdapter(imageDownloader) {
            val bundle = bundleOf(ARG_USERID to it.login)
            findNavController().navigate(R.id.action_searchUsersFragment_to_userFragment, bundle)
        }

        binding.userList.adapter = usersAdapter

        usersAdapter.addLoadStateListener {
            if (it.hasError) {
                searchUsersViewModel.statusLiveData.postValue(ServiceStatus.Error)
            } else if (it.append is LoadState.NotLoading) {
                if (it.append.endOfPaginationReached) {
                    searchUsersViewModel.statusLiveData.postValue(if (usersAdapter.itemCount < 1) ServiceStatus.SuccessEmpty else ServiceStatus.SuccessWithData)
                } else {
                    if (usersAdapter.itemCount > 0) {
                        searchUsersViewModel.statusLiveData.postValue(ServiceStatus.SuccessWithData)
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                searchUsersViewModel.users.collectLatest {
                    usersAdapter.submitData(it)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchUsersFragment().apply {
                arguments = Bundle()
            }
    }
}