package com.example.arthur.redcup;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Toast;

import com.example.arthur.redcup.View.LoginActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

    @RunWith(JUnit4.class)
    public class LoginActivityInstrumentedTest {


        @Rule
        public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class);

        @Test
        public void shouldValidateNullEmailLogin() {
            onView(withId(R.id.email))
                    .perform(typeText(""));
            closeSoftKeyboard();
            onView(withId(R.id.password))
                    .perform(typeText("123456"));
            closeSoftKeyboard();
            onView(withId(R.id.btn_login))
                    .perform(click());
            onView(withId(R.id.email)).check(matches(hasErrorText("Insira o seu email!")));

        }

        @Test
        public void shouldValidateNullEmailLoginAndNullPassword() {
            onView(withId(R.id.email))
                    .perform(typeText(""));
            closeSoftKeyboard();
            onView(withId(R.id.password))
                    .perform(typeText(""));
            closeSoftKeyboard();
            onView(withId(R.id.btn_login))
                    .perform(click());
            onView(withId(R.id.email)).check(matches(hasErrorText("Insira o seu email!")));

        }

        @Test
        public void shouldValidateNullPassword() {
            onView(withId(R.id.email))
                    .perform(typeText("Example_redcup@gmail.com"));
            closeSoftKeyboard();
            onView(withId(R.id.password))
                    .perform(typeText(""));
            closeSoftKeyboard();
            onView(withId(R.id.btn_login))
                    .perform(click());
            onView(withId(R.id.password)).check(matches(hasErrorText("Insira a sua senha!")));

        }

        @Test
        public void shouldValidatePasswordLess6() {
            onView(withId(R.id.email))
                    .perform(typeText("Example_redcup@gmail.com"));
            closeSoftKeyboard();
            onView(withId(R.id.password))
                    .perform(typeText("123"));
            closeSoftKeyboard();
            onView(withId(R.id.btn_login))
                    .perform(click());
            onView(withId(R.id.password)).check(matches(hasErrorText("Senha muito curta, minímo 6 caráteres!")));

        }

        @Test
        public void shouldValidatePasswordMore16() {
            onView(withId(R.id.email))
                    .perform(typeText("Example_redcup@gmail.com"));
            closeSoftKeyboard();
            onView(withId(R.id.password))
                    .perform(typeText("1234567888889999876543"));
            closeSoftKeyboard();
            onView(withId(R.id.btn_login))
                    .perform(click());
            onView(withId(R.id.password)).check(matches(hasErrorText("Senha muito longa, máximo 16 caráteres!")));

        }

/*        @Test
        public void shouldValidateEmailAndPasswordWrong() {
            onView(withId(R.id.email))
                    .perform(typeText("teste@gmail.com"));
            closeSoftKeyboard();
            onView(withId(R.id.password))
                    .perform(typeText("12345678"));
            closeSoftKeyboard();
            onView(withId(R.id.btn_login))
                    .perform(click());
            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
        }*/

/*        @Test
        public void shouldValidateEmailAndPasswordRight() {
            onView(withId(R.id.email))
                    .perform(typeText("Example_redcup@gmail.com"));
            closeSoftKeyboard();
            onView(withId(R.id.password))
                    .perform(typeText("123456"));
            closeSoftKeyboard();
            onView(withId(R.id.btn_login))
                    .perform(click());


        }*/

    }