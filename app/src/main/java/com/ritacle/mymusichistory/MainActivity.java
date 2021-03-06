package com.ritacle.mymusichistory;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.ritacle.mymusichistory.fragments.ListensFragment;
import com.ritacle.mymusichistory.fragments.topAlbums.TopAlbumsMainFragment;
import com.ritacle.mymusichistory.fragments.topArtists.TopArtistsMainFragment;
import com.ritacle.mymusichistory.fragments.topSongs.TopSongsMainFragment;
import com.ritacle.mymusichistory.service.ListenerService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentTransaction fragmentTransaction;
    private NavigationView navigationView;
    private MMHApplication application;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askForNotificationPermission();

        application = (MMHApplication) getApplication();
        application.startListenerService();

        SharedPreferences settings = getSharedPreferences("login", MODE_PRIVATE);
        settings.registerOnSharedPreferenceChangeListener(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        String username = settings.getString("userName", "User");
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTitle = headerView.findViewById(R.id.usernameView);
        usernameTitle.setText(username);

        String mail = settings.getString("mail", "");
        TextView mailTitle = headerView.findViewById(R.id.mailView);
        mailTitle.setText(mail);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new ListensFragment());
        fragmentTransaction.commit();

      /*  Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
        //serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
*/
    }

    private void askForNotificationPermission() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }

        if (!ListenerService.isNotificationAccessEnabled(this)) {
            alertDialog =
                    new AlertDialog.Builder(this)
                            .setTitle("Before we start")
                            .setMessage("The application needs access to notifications to continue working")
                            .setPositiveButton(
                                    android.R.string.ok,
                                    (dialogInterface, i) -> {
                                        String action;
                                        action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
                                        startActivity(new Intent(action));
                                    })
                            .show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setPositiveButton(
                            android.R.string.yes,
                            (dialog, whichButton) -> {
                                application.logout();
                                Intent intent = new Intent(this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.list_of_listens) {
            fragment = new ListensFragment();
        }
        if (id == R.id.nav_top_artists) {
            fragment = new TopArtistsMainFragment();
        }
        if (id == R.id.nav_top_albums) {
            fragment = new TopAlbumsMainFragment();
        }
        if (id == R.id.nav_top_songs) {
            fragment = new TopSongsMainFragment();

        }

        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("userName")) {
            String savedUsername = sharedPreferences.getString("userName", "User");
            View headerView = navigationView.getHeaderView(0);
            TextView title = headerView.findViewById(R.id.usernameView);
            title.setText(savedUsername);
        }
    }

}
