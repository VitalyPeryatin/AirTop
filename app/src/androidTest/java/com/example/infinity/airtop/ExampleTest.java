package com.example.infinity.airtop;

import android.support.test.runner.AndroidJUnit4;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class ExampleTest {
    @Test
    public void main(){
        String phone = PhoneNumberUtils.formatNumberToE164("65321381", "CHN");
        String phone2 = PhoneNumberUtils.stripSeparators("+(86 24) 2322 39 27");
        //boolean b = Patterns.PHONE.matcher(phone).matches();
        Log.d("mLog", phone + " " + phone2);
        //Log.d("mLog", ""+b);
    }
}
