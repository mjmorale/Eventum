package ch.epfl.sdp.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.ActivityMainBinding;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.platforms.google.map.GoogleLocationService;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.createevent.CreateEventActivity;
import ch.epfl.sdp.ui.event.EventActivity;
import ch.epfl.sdp.ui.main.attending.AttendingListFragment;
import ch.epfl.sdp.ui.main.map.MapFragment;
import ch.epfl.sdp.ui.main.swipe.SwipeFragment;
import ch.epfl.sdp.ui.settings.SettingsActivity;
import ch.epfl.sdp.ui.user.UserActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private final static int PERMISSION_LOCATION = 0;
    private FilterSettingsViewModel.FilterSettingsViewModelFactory mFilterSettingsFactory;

    private ActivityMainBinding mBinding;
    private FilterSettingsViewModel mFilterSettingsViewModel;
    private LocationService mLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        mFilterSettingsFactory = new FilterSettingsViewModel.FilterSettingsViewModelFactory();
        mFilterSettingsFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
        mFilterSettingsViewModel = new ViewModelProvider(this, mFilterSettingsFactory).get(FilterSettingsViewModel.class);

        View view = mBinding.getRoot();
        setContentView(view);
        setSupportActionBar(mBinding.mainToolbar);

        mBinding.mainNavView.setNavigationItemSelectedListener(this);
        mBinding.mainNavView.getHeaderView(0).findViewById(R.id.main_nav_header_layout).setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.mainDrawerLayout, mBinding.mainToolbar,
                R.string.navigation_main_drawer_open, R.string.navigation_main_drawer_close);
        mBinding.mainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        GoogleLocationService.initService((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        mLocationService = GoogleLocationService.getInstance();

        if (savedInstanceState == null) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int result: grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new SwipeFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.main_actionbar_add:
                Intent intent = new Intent(this, CreateEventActivity.class);
                startActivityForResult(intent, UIConstants.RC_CREATE_EVENT);
                break;
            case R.id.main_actionbar_search:
                mBinding.menuMainSearch.mSeekBarRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mBinding.menuMainSearch.mSeekBarValue.setText(progress + "km");
                        Location location = mLocationService.getLastKnownLocation(getApplicationContext());
                        mFilterSettingsViewModel.setSettings(location, (double) progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });
                mBinding.menuMainSearch.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    public TextView getSeekBarValue() {
        return mBinding.menuMainSearch.mSeekBarValue;
    }

    public SeekBar getSeekBarRange() {
        return mBinding.menuMainSearch.mSeekBarRange;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UIConstants.RC_CREATE_EVENT) {
            if(resultCode == RESULT_OK) {
                String eventRef = data.getStringExtra(UIConstants.BUNDLE_EVENT_REF);
                Intent eventIntent = new Intent(this, EventActivity.class);
                eventIntent.putExtra(UIConstants.BUNDLE_EVENT_MODE_REF, EventActivity.EventActivityMode.ORGANIZER);
                eventIntent.putExtra(UIConstants.BUNDLE_EVENT_REF, eventRef);
                startActivity(eventIntent);
            }
            else {
                Toast.makeText(this, "Failed to create event", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(mBinding.mainContainer.getId(), new SwipeFragment()).commit();
                break;
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction()
                        .replace(mBinding.mainContainer.getId(), new MapFragment()).commit();
                break;
            case R.id.nav_attending:
                getSupportFragmentManager().beginTransaction()
                        .replace(mBinding.mainContainer.getId(), new AttendingListFragment()).commit();
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        mBinding.mainDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        // If the drawer is open and the back button is pressed, then
        // close the drawer instead of quitting the activity
        if(mBinding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.mainDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.main_nav_header_layout:
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                break;
        }
    }
}
