package net.telepathix.githubbrowse.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.telepathix.githubbrowse.R
import net.telepathix.githubbrowse.databinding.UserFragmentBinding
import net.telepathix.githubbrowse.image_download.ImageDownloader
import net.telepathix.githubbrowse.ui.handleServiceStatus
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var userId: String
    private val userViewModel: UserViewModel by viewModels()
    private var _binding: UserFragmentBinding? = null
    //onCreateViewへの呼び出しとonDestroyViewへの呼び出しの間飲み、利用できます
    private val binding get() = _binding!!
    @Inject lateinit var imageDownloader: ImageDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(ARG_USERID)!!
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = UserFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        initAdapter()
        userViewModel.userLiveData.observe(viewLifecycleOwner) {
            if (it.name?.isNotEmpty() == true) {
                binding.userTitle.text = "${it.login}(${it.name})"
            } else {
                val fullNameNotSet = context?.resources?.getString(R.string.no_fullname) ?: ""
                binding.userTitle.text = "${it.login}($fullNameNotSet)"
            }
            binding.followerCount.text = "${it.followers}"
            binding.followingCount.text = "${it.following}"
            imageDownloader.downloadImage(requireContext(), it.avatarUrl, binding.userAvatar)
        }
        handleServiceStatus(binding.repositoryList, binding.loadingIndicator, binding.message, R.string.no_respository,
            R.string.error_message, userViewModel.statusLiveData, viewLifecycleOwner)
        userViewModel.getDataForUser(userId)
        return view
    }

    fun initAdapter() {
        val repositoriesAdapter = RepositoriesAdapter {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl)))
        }
        binding.repositoryList.adapter = repositoriesAdapter
        userViewModel.repositoriesLiveData.observe(viewLifecycleOwner) {
            repositoriesAdapter.repositories = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_USERID = "userId"

        @JvmStatic
        fun newInstance(userId: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERID, userId)
                }
            }
    }
}