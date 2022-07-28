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
class TestModuloEscritor {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun testModuloEscritor() {
        val actionMenuItemView = onView(
            allOf(
                withId(R.id.menu_escritor), withContentDescription("Escritor"),
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

        val cardView = onView(
            allOf(
                withId(R.id.agregarUsuarioCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    0
                )
            )
        )
        cardView.perform(scrollTo(), click())

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.buttomregresar),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val relativeLayout = onView(
            allOf(
                withId(R.id.panntalla_MasterMenu),
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
                withId(R.id.panntalla_MasterMenu),
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

        val cardView2 = onView(
            allOf(
                withId(R.id.agregarUsuarioCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    0
                )
            )
        )
        cardView2.perform(scrollTo(), click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.buttomregresar),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val cardView3 = onView(
            allOf(
                withId(R.id.eliminarUsuarioCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                )
            )
        )
        cardView3.perform(scrollTo(), click())

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.rbuttomregresar),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        val cardView4 = onView(
            allOf(
                withId(R.id.crearLibroCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    2
                )
            )
        )
        cardView4.perform(scrollTo(), click())

        val appCompatImageButton4 = onView(
            allOf(
                withId(R.id.buttomregresar),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        val cardView5 = onView(
            allOf(
                withId(R.id.agregarPoesiaCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    3
                )
            )
        )
        cardView5.perform(scrollTo(), click())

        val appCompatImageButton5 = onView(
            allOf(
                withId(R.id.buttomregresar),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton5.perform(click())

        val cardView6 = onView(
            allOf(
                withId(R.id.agregarArchivosCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    4
                )
            )
        )
        cardView6.perform(scrollTo(), click())

        val appCompatImageButton6 = onView(
            allOf(
                withId(R.id.buttomregresar),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton6.perform(click())

        val cardView7 = onView(
            allOf(
                withId(R.id.agregarPdfCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    5
                )
            )
        )
        cardView7.perform(scrollTo(), click())

        val appCompatImageButton7 = onView(
            allOf(
                withId(R.id.buttomregresar),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton7.perform(click())

        val cardView8 = onView(
            allOf(
                withId(R.id.agregarTxtCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    7
                )
            )
        )
        cardView8.perform(scrollTo(), click())

        val appCompatImageButton8 = onView(
            allOf(
                withId(R.id.buttomregresar),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton8.perform(click())

        val cardView9 = onView(
            allOf(
                withId(R.id.eliminarContenidoCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    6
                )
            )
        )
        cardView9.perform(scrollTo(), click())

        val recyclerView = onView(
            allOf(
                withId(R.id.categoriesRv),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.booksRv),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.menu_casa), withContentDescription("Escritor"),
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

        val cardView10 = onView(
            allOf(
                withId(R.id.cerrarsecionCarview),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    8
                )
            )
        )
        cardView10.perform(scrollTo(), click())

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

        val relativeLayout4 = onView(
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
        relativeLayout4.check(matches(isDisplayed()))
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
