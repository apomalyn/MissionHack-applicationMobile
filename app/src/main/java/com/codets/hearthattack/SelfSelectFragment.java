package com.codets.hearthattack;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.OnboardingSupportFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfSelectFragment extends OnboardingSupportFragment {

    @Override
    protected int getPageCount() {
        return 1;
    }

    @Override
    protected CharSequence getPageTitle(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return "Welcome !";
            case 1:
                return "Autorisation please";
            default:
                return "CodETS";
        }
    }

    @Override
    protected CharSequence getPageDescription(int pageIndex) {
        return null;
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        /*ImageView mContentView = new ImageView(getContext());
        mContentView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mContentView.setImageResource(R.drawable.ic_settings_black_24dp);
        mContentView.setPadding(0, 32, 0, 32);*/
        View view = inflater.inflate(R.layout.welcome, container, false);
        TextView textView = view.findViewById(R.id.welcome);

        textView.setText(getContext().getString(R.string.welcome_content));

        return textView;
    }


    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }


    @Override
    protected void onFinishFragment() {
        super.onFinishFragment();
        // User has seen OnboardingFragment, so mark our SharedPreferences
        // flag as completed so that we don't show our OnboardingFragment
        // the next time the user launches the app.
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        sharedPreferencesEditor.putBoolean(
                SelfSelectActivity.COMPLETED_ONBOARDING_PREF_NAME, true);
        sharedPreferencesEditor.apply();
    }
}
