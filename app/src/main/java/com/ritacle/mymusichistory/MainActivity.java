package com.ritacle.mymusichistory;

import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.preference.PreferenceManager;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.ritacle.mymusichistory.fragments.ListensFragment;
import com.ritacle.mymusichistory.fragments.topArtists.TopArtistsMainFragment;
import com.ritacle.mymusichistory.fragments.topSongs.TopSongsMainFragment;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.service.ListeningBroadcastReceiver;
import com.ritacle.mymusichistory.service.SendService;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST_CODE_EMAIL = 1;
    private BlockingDeque<Scrobble> listens;
    private SendService sendService;
    private String accountName;
    private FragmentTransaction fragmentTransaction;
    private NavigationView navigationView;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        accountName = sharedPref.getString(getResources().getString(R.string.account_key), null);

        if (accountName == null) {
            getUser();
        }

//TODO: prevent exception when account name has not been defined yet during the first run

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        String username = settings.getString("user_name", "User");
        View headerView = navigationView.getHeaderView(0);
        TextView title = headerView.findViewById(R.id.usernameView);
        title.setText(username);


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new ListensFragment());
        fragmentTransaction.commit();


      /*  Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
        //serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
*/

   /*     RecyclerView rvListens = (RecyclerView) findViewById(R.id.rvListens);
        ListenAdapter adapter = new ListenAdapter(listenService.getListens());
        rvListens.setAdapter(adapter);
        rvListens.addItemDecoration(new SimpleDividerItemDecoration(this));
        rvListens.setLayoutManager(new LinearLayoutManager(this));
*/


        listens = new LinkedBlockingDeque<>();
        sendService = new SendService(getApplicationContext(), listens);

        // ArrayAdapter<Listen> listAdapter = new ArrayAdapter<Listen>.createFromResource(getApplicationContext(),)
        // listenHistoryView.setAdapter(listAdapter);

        registerReceiver(new ListeningBroadcastReceiver(listens, this), createFilter());
        performOnBackgroundThread(sendService);
        //startService(new Intent(getApplicationContext(), ListenerService.class));

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
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
            fragment = new TopAlbumsFragment();
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

    private void getUser() {

        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},
                false, null, null, null, null);

        try {
            startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            // This device may not have Google Play Services installed.
            // TODO: do something else
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            getPreferences(Context.MODE_PRIVATE).edit().putString(getResources().getString(R.string.account_key), accountName).apply();
        }
    }

    public String getAccountName() {
        return accountName;
    }


    private void performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();

    }

    private IntentFilter createFilter() {
        IntentFilter intentFilter = new IntentFilter();

        //  intentFilter.addAction("com.android.music.musicservicecommand");
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.updateprogress");
       /*
        intentFilter.addAction("com.htc.music.metachanged");
        intentFilter.addAction("fm.last.android.metachanged");
        intentFilter.addAction("com.sec.android.app.music.metachanged");
        intentFilter.addAction("com.nullsoft.winamp.metachanged");
        intentFilter.addAction("com.amazon.mp3.metachanged");
        intentFilter.addAction("com.miui.player.metachanged");
        intentFilter.addAction("com.real.IMP.metachanged");
        intentFilter.addAction("com.sonyericsson.music.metachanged");
        intentFilter.addAction("com.rdio.android.metachanged");
        intentFilter.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        intentFilter.addAction("com.andrew.apollo.metachanged");*/

        return intentFilter;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("user_name")) {
            String savedUsername = sharedPreferences.getString("user_name", "User");
            View headerView = navigationView.getHeaderView(0);
            TextView title = headerView.findViewById(R.id.usernameView);
            title.setText(savedUsername);
        }
    }

}
