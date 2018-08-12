package com.example.infinity.airtop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;

import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.ui.chat.ChatActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {
    @Rule
    public ActivityTestRule<ChatActivity> activity = new ActivityTestRule<>(
            ChatActivity.class, true, false);

    private SharedPreferences authPref;
    private SharedPreferences mainPref;
    private static final Intent MY_ACTIVITY_INTENT = new Intent(
            InstrumentationRegistry.getTargetContext(), ChatActivity.class);

    @Before
    public void setUp(){
        authPref = getInstrumentation().getContext().getSharedPreferences("authPref_test", Context.MODE_PRIVATE);
        mainPref = getInstrumentation().getContext().getSharedPreferences("mainPref_test", Context.MODE_PRIVATE);
        User user = new User("+79035724917");
        user.uuid = "2340981024-324234-3242-34-1423423424";
        App.getInstance().setCurrentUser(user);

        activity.launchActivity(MY_ACTIVITY_INTENT);
    }

    @Test
    public void main(){
        onView(withId(R.id.editText))
                .check(matches(allOf(isDisplayed(), withText(""))))
                .perform(typeText("Hello"))
                .check(matches(withText("Hello")));
        onView(withId(R.id.btnSend))
                .perform(click());
        onView(withId(R.id.editText))
                .check(matches(withText("")));
    }

    @After
    public void finalize(){
        authPref.edit().clear().apply();
        mainPref.edit().clear().apply();
    }
}
