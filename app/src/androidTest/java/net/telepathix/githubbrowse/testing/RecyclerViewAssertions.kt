package net.telepathix.githubbrowse.testing

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers

class RecyclerViewAssertions {
    companion object {
        fun hasItemCount(count: Int): ViewAssertion {
            return RecyclerViewItemCountAssertion(count)
        }
    }

    private class RecyclerViewItemCountAssertion(private val count: Int) : ViewAssertion {

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            if (view !is RecyclerView) {
                throw IllegalStateException("The asserted view is not a RecyclerView")
            }

            if (view.adapter == null) {
                throw IllegalStateException("There is no adapter assigned to the RecyclerView")
            }

            ViewMatchers.assertThat("RecyclerView item count", view.adapter?.itemCount, CoreMatchers.equalTo(count))
        }
    }
}