package com.arboleda.tifloapp


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    // esta variable es global para todas las funciones que se creen

   @get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun checkActivityvisibility(){

        onView(withId(R.id.pantalla_MainActivity))
            .check(matches(isDisplayed()))

    }

    @Test
    fun checkElementActivityvisibility(){

        onView(withId(R.id.bottomactualizar))
            .check(matches(isDisplayed()))

        onView(withId(R.id.recyclerLibros))
            .check(matches(isDisplayed()))

        onView(withId(R.id.ReconocerBottom))
            .check(matches(isDisplayed()))
    }





}



