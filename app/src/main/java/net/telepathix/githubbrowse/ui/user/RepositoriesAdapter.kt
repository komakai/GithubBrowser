package net.telepathix.githubbrowse.ui.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import net.telepathix.githubbrowse.R
import net.telepathix.githubbrowse.databinding.RepositoryItemBinding
import net.telepathix.githubbrowse.service.models.Repository

//ヘルパー拡張関数
fun TextView.setTextOrDefault(value: String?, @StringRes stringResIfEmpty: Int) {
    if (value?.isNotEmpty() == true) {
        text = value
        setTextColor(ContextCompat.getColor(context, R.color.black))
    } else {
        text = context.resources.getString(stringResIfEmpty)
        setTextColor(ContextCompat.getColor(context, R.color.gray_light))
    }
}

class RepositoriesAdapter(private val clickHandler: (Repository) -> (Unit))
    : RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder>() {

    var repositories: List<Repository> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.repository_item, parent, false)
        return RepositoryViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = repositories[position]
        repository.let {
            val context = holder.binding.root.context
            holder.binding.repositoryName.text = it.name
            holder.binding.repositoryDescription.setTextOrDefault(it.description, R.string.no_description)
            holder.binding.language.setTextOrDefault(it.language, R.string.no_language)
            val starGraphic = ContextCompat.getDrawable(context, R.drawable.star)
            starGraphic?.setTint(ContextCompat.getColor(context, if (it.starCount > 0) R.color.gold_star else R.color.gray_dark))
            holder.binding.star.setImageDrawable(starGraphic)
            holder.binding.starCount.text = "${it.starCount}"
            holder.binding.root.setOnClickListener {
                clickHandler(repository)
            }
        }
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RepositoryItemBinding.bind(itemView)
    }
}