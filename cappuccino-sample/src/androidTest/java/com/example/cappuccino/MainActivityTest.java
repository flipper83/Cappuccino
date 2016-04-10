package com.example.cappuccino;

import com.metova.cappuccino.Cappuccino;
import com.metova.cappuccino.CappuccinoIdlingResource;
import com.metova.cappuccino.fluent.CappuccinoInteraction;
import com.metova.cappuccino.fluent.OngoingInteraction;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class MainActivityTest {

    ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setUp() throws Exception {
        mActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        Cappuccino.reset();
    }

    @Test
    public void testEspressoWay() throws Exception {
        // Instantiate and register the IdlingResource
        CappuccinoIdlingResource idlingResource = new CappuccinoIdlingResource(mActivityTestRule.getActivity());
        Espresso.registerIdlingResources(idlingResource);

        // This view animates in
        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

        // Unregister the IdlingResource
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void testCappuccinoWay() throws Exception {
        // Register the IdlingResource
        Cappuccino.registerIdlingResource(mActivityTestRule.getActivity());

        // This view animates in
        onView(withId(R.id.text_hello)).check(matches(isDisplayed()));

        // Unregister the IdlingResource
        Cappuccino.unregisterIdlingResource(mActivityTestRule.getActivity());
    }

    @Test
    public void testMultipleActivities() throws Exception {
        // Tapping this will take us to the second activity
        onView(withId(R.id.button_second_activity)).perform(click());

        // Register the IdlingResource
        Cappuccino.registerIdlingResource(MainActivity.RESOURCE_MULTIPLE_ACTIVITIES);

        // This view animates in
        onView(withId(R.id.text_second)).check(matches(isDisplayed()));

        // Unregister the IdlingResource
        Cappuccino.unregisterIdlingResource(MainActivity.RESOURCE_MULTIPLE_ACTIVITIES);
    }

    @Test
    public void testFluentInteraction2() throws Exception {
        // There is an implied "allOf(...)" relationship when matching

        OngoingInteraction.onView()
                .withId(R.id.text_fluent)
                .withText(R.string.text_fluent)
//                .beginAssert()
                .check()
                .isDisplayed()
                .not().isFocusable()
                .isClickable();
//                .endAssert();
    }

    @Test
    public void testFluentInteractionDoesNotExist() throws Exception {
        // There is an implied "allOf(...)" relationship when matching

        OngoingInteraction.onView()
                .withId(R.id.text_hello)
                .withText(R.string.hello)
                .beginAssert()
//                .check()
                .doesNotExist()
                .endAssert();
    }

    @Test
    public void testFluentInteraction() throws Exception {
        // There is an implied "allOf(...)" relationship when matching

        CappuccinoInteraction.onView()
                .withId(R.id.text_fluent)
                .withText(R.string.text_fluent)
                .check()
                .isDisplayed()
                .not().isFocusable()
                .is(ViewMatchers.isClickable()); // a "custom" ViewMatcher
//                .isClickable();
    }

    @Test
    public void testFluentToEspressoInteraction() throws Exception {
        CappuccinoInteraction.onView()
                .withId(R.id.text_fluent)
                .toEspresso().check(matches(isDisplayed()));

        CappuccinoInteraction.onView()
                .withId(R.id.text_fluent)
                .espressoCheck(matches(isDisplayed()));
    }
}