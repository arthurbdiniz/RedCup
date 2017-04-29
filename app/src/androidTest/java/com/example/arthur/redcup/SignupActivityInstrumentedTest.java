package com.example.arthur.redcup;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Toast;

import com.example.arthur.redcup.View.LoginActivity;
import com.example.arthur.redcup.View.SignupActivity;

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
public class SignupActivityInstrumentedTest {


    @Rule
    public ActivityTestRule<SignupActivity> rule = new ActivityTestRule<>(SignupActivity.class);

/*    @Test
    public void shouldValidateNullEmailLogin() {
        onView(withId(R.id.email))
                .perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.password))
                .perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Insira o seu email!")));

    }

    @Test
    public void shouldValidateNullPassword() {
        onView(withId(R.id.email))
                .perform(typeText("Teste@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.password))
                .perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Insira a sua senha!")));

    }

    @Test
    public void shouldValidateNullPasswordLess6() {
        onView(withId(R.id.email))
                .perform(typeText("Teste"));
        closeSoftKeyboard();
        onView(withId(R.id.password))
                .perform(typeText("123"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Senha muito curta, minímo 6 carácteres!")));

    }

    @Test
    public void shouldValidateNullPasswordMore16() {
        onView(withId(R.id.email))
                .perform(typeText("Teste@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.password))
                .perform(typeText("12345678900987765543323456754"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Senha muito longa, máximo 16 caráteres!")));

    }

    @Test
    public void shouldValidateNullPasswordAndEmailWrong() {
        onView(withId(R.id.email))
                .perform(typeText("Teste"));
        closeSoftKeyboard();
        onView(withId(R.id.password))
                .perform(typeText("1234567"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_button))
                .perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText("Falha na autenticação, cheque o email e a senha.")));
       **** TOAST ****
    }*/

    @Test
    public void shouldValidateNullPasswordAndEmailRight() {
        onView(withId(R.id.email))
                .perform(typeText("Teste@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.password))
                .perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.sign_in_button))
                .perform(click());


    }
}