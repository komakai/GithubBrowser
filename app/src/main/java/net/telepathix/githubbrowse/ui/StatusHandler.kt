package net.telepathix.githubbrowse.ui

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun handleServiceStatus(
    resultView: View,
    progressView: View,
    messageView: TextView,
    @StringRes emptyMessage: Int,
    @StringRes errorMessage: Int,
    status: LiveData<ServiceStatus>,
    viewLifecycleOwner: LifecycleOwner
)
{
    status.observe(viewLifecycleOwner) {
        when (it) {
            ServiceStatus.None -> {
                showHideViews(listOf(resultView), listOf(messageView, progressView))
            }
            ServiceStatus.InProgress -> {
                showHideViews(listOf(progressView), listOf(messageView, resultView))
            }
            ServiceStatus.SuccessWithData -> {
                showHideViews(listOf(resultView), listOf(messageView, progressView))
            }
            ServiceStatus.SuccessEmpty -> {
                showHideViews(listOf(messageView), listOf(progressView, resultView))
                setMessage(messageView,  emptyMessage)
            }
            ServiceStatus.Error -> {
                showHideViews(listOf(messageView), listOf(progressView, resultView))
                setMessage(messageView, errorMessage)
            }
        }
    }

}

fun showHideViews(show: List<View>, hide: List<View>) {
    show.forEach { it.visibility = View.VISIBLE }
    hide.forEach { it.visibility = View.INVISIBLE }
}

fun setMessage(messageView: TextView, @StringRes messageRes: Int) {
    messageView.text = messageView.context?.resources?.getString(messageRes)
}
