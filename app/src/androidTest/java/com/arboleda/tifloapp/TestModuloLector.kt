package com.arboleda.tifloapp


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestModuloLector {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun testModuloLector() {
        val relativeLayout = onView(
            allOf(
                withId(R.id.pantalla_MainActivity),
                withParent(
                    allOf(
                        withId(android.R.id.content),
                        withParent(withId(R.id.decor_content_parent))
                    )
                ),
                isDisplayed()
            )
        )
        relativeLayout.check(matches(isDisplayed()))

        val relativeLayout2 = onView(
            allOf(
                withId(R.id.pantalla_MainActivity),
                withParent(
                    allOf(
                        withId(android.R.id.content),
                        withParent(withId(R.id.decor_content_parent))
                    )
                ),
                isDisplayed()
            )
        )
        relativeLayout2.check(matches(isDisplayed()))

        val relativeLayout3 = onView(
            allOf(
                withId(R.id.pantalla_MainActivity),
                withParent(
                    allOf(
                        withId(android.R.id.content),
                        withParent(withId(R.id.decor_content_parent))
                    )
                ),
                isDisplayed()
            )
        )
        relativeLayout3.check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.recyclerLibros),
                childAtPosition(
                    withId(R.id.pantalla_MainActivity),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.descripcionbutton),
                withContentDescription("Desplegar descripción del libro"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.carviewpantalla),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val materialButton = onView(
            allOf(
                withId(android.R.id.button1), withText("Cerrar descripción"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.recycleruser1),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    2
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(2, click()))

        val recyclerView3 = onView(
            allOf(
                withId(R.id.recycleruser2),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatImageView = onView(
            allOf(
                withId(R.id.exo_replay), withContentDescription("Repetir el video"),
                childAtPosition(
                    allOf(
                        withId(R.id.sec_controlvid1),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.buttomregresar),
                withContentDescription("Regresar a los archivos de la poesia"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbarRl),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.volver), withContentDescription("Regresar"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val recyclerView4 = onView(
            allOf(
                withId(R.id.recycleruser1),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    2
                )
            )
        )
        recyclerView4.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val recyclerView5 = onView(
            allOf(
                withId(R.id.recycleruser2),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        )
        recyclerView5.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.exo_ffwd), withContentDescription("Adelantar 5 segundos"),
                childAtPosition(
                    allOf(
                        withId(R.id.sec_controlvid1),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.buttomregresar),
                withContentDescription("Regresar a los archivos de la poesia"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbarRl),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.volver), withContentDescription("Regresar"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView2.perform(click())

        val actionMenuItemView3 = onView(
            allOf(
                withId(R.id.volver), withContentDescription("Regresar"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView3.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
