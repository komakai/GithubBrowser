@file:Suppress("DEPRECATION")
package net.telepathix.githubbrowse

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.telepathix.githubbrowse.testing.RecyclerViewAssertions.Companion.hasItemCount

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UITests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testUserSearchSelection() {
        onView(withId(R.id.userList)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.searchBox))
            .perform(replaceText("koma"))
            .perform(pressImeActionButton())
        onView(withId(R.id.userList)).check(hasItemCount(10))
        onView(withText("koma01"))
            .perform(click())
        onView(withId(R.id.userTitle)).check(matches(withText("koma01(Koma Neko)")))
        onView(withId(R.id.followerCount)).check(matches(withText("12")))
        onView(withId(R.id.followingCount)).check(matches(withText("5")))
        onView(withId(R.id.repositoryList)).check(hasItemCount(4))  // forkedリポジトリが表示されてない！！
        pressBack()
        onView(withText("koma02"))
            .perform(click())
        onView(withId(R.id.repositoryList)).check(hasItemCount(0))  // forkedリポジトリが表示されてない！！
        onView(withId(R.id.message)).check(matches(withText("リポジトリがありません")))
        pressBack()
    }

    @Test
    fun testErrorMessage() {
        onView(withId(R.id.searchBox))
            .perform(replaceText("simulate_error"))
            .perform(pressImeActionButton())
        onView(withId(R.id.message)).check(matches(withText("エラーが発生しました")))
    }

    @Test
    fun testNoMatchesMessage() {
        onView(withId(R.id.searchBox))
            .perform(replaceText("xxxxxxxx"))
            .perform(pressImeActionButton())
        onView(withId(R.id.message)).check(matches(withText("一致するユーザーがありません")))
    }

}