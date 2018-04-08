package com.codets.hearthattack;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private final static int DIAGNOSTICS_FRAGMENT = 0;
    private final static int DASHBOARD_FRAGMENT = 1;
    private final static int SETTINGS_FRAGMENT = 1;

    private EmergencyService emergencyService;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_diagnostics:
                    switchView(DIAGNOSTICS_FRAGMENT);
                    return true;
                case R.id.navigation_dashboard:
                    switchView(DASHBOARD_FRAGMENT);
                    return true;
                case R.id.navigation_settings:
                    switchView(SETTINGS_FRAGMENT);
                    return true;
            }
            return false;
        }
    };

    private void switchView(int code){
        FragmentManager manager = getSupportFragmentManager();

        Fragment fragment = null;
        switch (code){
            case DIAGNOSTICS_FRAGMENT:
                fragment = new DiagnosticsFragment();
                break;
            case DASHBOARD_FRAGMENT:
                fragment = new DashboardFragment();
        }
        manager.beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        emergencyService = new EmergencyService();
        emergencyService.start();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new DashboardFragment()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
