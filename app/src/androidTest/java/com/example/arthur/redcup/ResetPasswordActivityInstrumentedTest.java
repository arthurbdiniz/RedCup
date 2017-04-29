package com.example.arthur.redcup;


import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Toast;

import com.example.arthur.redcup.View.LoginActivity;
import com.example.arthur.redcup.View.ResetPasswordActivity;
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
public class ResetPasswordActivityInstrumentedTest {


    @Rule
    public ActivityTestRule<ResetPasswordActivity> rule = new ActivityTestRule<>(ResetPasswordActivity.class);

    @Test
    public void shouldValidateNullEmailLogin() {
        onView(withId(R.id.email))
                .perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.btn_reset_password))
                .perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Insira sua identificação de email registrada.")));

    }
/*

    @Test
    public void shouldValidateEmailLoginWrong() {
        onView(withId(R.id.email))
                .perform(typeText("Exemplo@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.btn_reset_password))
                .perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Falha ao enviar email para redefinição de senha.")));
        *****TOAST******
    }
*/

/*    @Test
    public void shouldValidateEmailLoginRight() {
        onView(withId(R.id.email))
                .perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.btn_reset_password))
                .perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText("Enviamos instruções para redefinir sua senha!")));
        ***Toast****
    }*/
}