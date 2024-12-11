package net.telepathix.githubbrowse.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.telepathix.githubbrowse.R
import net.telepathix.githubbrowse.databinding.UserItemBinding
import net.telepathix.githubbrowse.image_download.ImageDownloader
import net.telepathix.githubbrowse.service.models.User

class UsersAdapter(
    private val imageDownloader: ImageDownloader,
    private val clickHandler:(User) -> (Unit)
): PagingDataAdapter<User, UsersAdapter.UserViewHolder>(usersDiffCallback) {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = UserItemBinding.bind(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user?.let {
            imageDownloader.downloadImage(holder.binding.root.context, it.avatarUrl, holder.binding.userAvatar)
            holder.binding.userId.text = it.login
            holder.binding.root.setOnClickListener {
                clickHandler(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    companion object {
        val usersDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
        }
    }
}