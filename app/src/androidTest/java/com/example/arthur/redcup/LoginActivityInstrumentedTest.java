package com.example.arthur.redcup;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.arthur.redcup.View.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
    public class LoginActivityInstrumentedTest {

    int NUMBER_OF_ITENS_LISTVIEW = 7;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void shouldValidateNullEmailLogin() {
        onView(withId(R.id.email)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.password)).perform(typeText("senha"));
        closeSoftKeyboard();
        onView(withId(R.id.btn_login))
                .perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Enter email address!")));


    }

}
