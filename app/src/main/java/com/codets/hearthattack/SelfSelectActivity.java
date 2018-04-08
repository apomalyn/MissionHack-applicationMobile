package com.codets.hearthattack;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SelfSelectActivity extends FragmentActivity {

    public final static String COMPLETED_ONBOARDING_PREF_NAME = "SELF_SELECT_FINISHED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_select);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SelfSelectFragment()).commit();
    }
}
