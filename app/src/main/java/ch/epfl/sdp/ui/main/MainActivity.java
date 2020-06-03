package ch.epfl.sdp.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.navigation.NavigationView;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.ActivityMainBinding;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.platforms.android.AndroidConnectivityLiveData;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;
import ch.epfl.sdp.platforms.google.map.GoogleLocationService;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.createevent.CreateEventActivity;
import ch.epfl.sdp.ui.event.EventActivity;
import ch.epfl.sdp.ui.main.attending.AttendingListFragment;
import ch.epfl.sdp.ui.main.map.MapFragment;
import ch.epfl.sdp.ui.main.swipe.SwipeFragment;
import ch.epfl.sdp.ui.settings.FilterView;
import ch.epfl.sdp.ui.settings.SettingsActivity;
import ch.epfl.sdp.ui.user.UserActivity;


/**
 * The main activity of the application
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private final static int PERMISSION_LOCATION = 0;
    private FilterSettingsViewModel.FilterSettingsViewModelFactory mFilterSettingsFactory;

    private ActivityMainBinding mBinding;
    private FilterSettingsViewModel mFilterSettingsViewModel;
    private MainViewModel mViewModel;
    private final MainViewModel.MainViewModelFactory mFactory;

    private View mMainNavHeaderView;

    private AndroidConnectivityLiveData mConnectivityLiveData;

    /**
     * Constructor of the main activity
     */
    public MainActivity() {
        mFactory = new MainViewModel.MainViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setupFilterSettingsFactory();
        mFilterSettingsViewModel = new ViewModelProvider(this, mFilterSettingsFactory).get(FilterSettingsViewModel.class);

        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.mainToolbar);

        setupToolbarNavigation();
        // Build the view model
        mFactory.setUserRef(getUserRefFromIntent(getIntent()));
        mViewModel = new ViewModelProvider(this, mFactory).get(MainViewModel.class);
        mViewModel.getUser().observe(this, user -> {
            if (user != null) {
                TextView username = mMainNavHeaderView.findViewById(R.id.main_nav_header_username);
                TextView email = mMainNavHeaderView.findViewById(R.id.main_nav_header_email);
                if(!user.getImageId().isEmpty())
                    ImageGetter.getInstance().getImage(getApplicationContext(), user.getImageId(),mMainNavHeaderView.findViewById(R.id.main_nav_header_profile_picture));
                username.setText(user.getName());
                email.setText(user.getEmail()); }
        });

        addFilterSeekBarSettingsListener();
        addFilterCategorySettingsListener();
        if (savedInstanceState == null) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_LOCATION); }
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
                Intent intent = getUserRefIntent(CreateEventActivity.class);

                startActivityForResult(intent, UIConstants.RC_CREATE_EVENT);
                break;
            case R.id.main_actionbar_search:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UIConstants.RC_CREATE_EVENT) {
            if(resultCode == RESULT_OK) {
                String eventRef = data.getStringExtra(UIConstants.BUNDLE_EVENT_REF);
                Intent eventIntent = getUserRefIntent(EventActivity.class);
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
                Intent intent = getUserRefIntent(SettingsActivity.class);

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
                Intent intent = getUserRefIntent(UserActivity.class);

                startActivity(intent);
                break;
        }
    }

    public void addFilterSeekBarSettingsListener() {
        mBinding.menuMainSearch.mSeekBarRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progressChanged = FilterView.MIN_VALUE + progress;
                mBinding.menuMainSearch.mSeekBarValue.setText(progressChanged + "km");
                mFilterSettingsViewModel.setSettings(getApplicationContext(), (double) progress + FilterView.MIN_VALUE,null,null);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void addFilterCategorySettingsListener() {
        mBinding.menuMainSearch.mOptionOutdoor.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilterSettingsViewModel.setSettings(getApplicationContext(), null, "Outdoor",b);
            }
        });
        mBinding.menuMainSearch.mOptionIndoor.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilterSettingsViewModel.setSettings(getApplicationContext(), null, "Indoor",b);
            }
        });
        mBinding.menuMainSearch.mOptionParty.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilterSettingsViewModel.setSettings(getApplicationContext(), null, "Party",b);
            }
        });
        mBinding.menuMainSearch.mOptionSport.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilterSettingsViewModel.setSettings(getApplicationContext(), null, "Sport",b);
            }
        });
    }

    public void setupToolbarNavigation() {
        setSupportActionBar(mBinding.mainToolbar);

        mBinding.mainNavView.setNavigationItemSelectedListener(this);
        mMainNavHeaderView = mBinding.mainNavView.getHeaderView(0).findViewById(R.id.main_nav_header_layout);
        mMainNavHeaderView.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.mainDrawerLayout, mBinding.mainToolbar,
                R.string.navigation_main_drawer_open, R.string.navigation_main_drawer_close);
        mBinding.mainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupFilterSettingsFactory() {
        LocationService locationService = new GoogleLocationService((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        mFilterSettingsFactory = new FilterSettingsViewModel.FilterSettingsViewModelFactory();
        mFilterSettingsFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
        mFilterSettingsFactory.setLocationService(locationService);
        mFilterSettingsFactory.setAuthenticator(ServiceProvider.getInstance().getAuthenticator());
    }

    private String getUserRefFromIntent(Intent intent) {
        // Preferences are used to store the user reference to persistent storage
        // to handle the situation where the activity is destroyed and need to be
        // rebuilt.
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

        String userRef;
        if(intent.hasExtra(UIConstants.BUNDLE_USER_REF)) {
            // Load the user reference from the intent if possible
            userRef = intent.getStringExtra(UIConstants.BUNDLE_USER_REF);
            // Update the content of the persistent storage
            preferences.edit().putString(UIConstants.BUNDLE_USER_REF, userRef).apply();
        }
        else {
            // If the intent is empty, then load from persistent storage
            userRef = preferences.getString(UIConstants.BUNDLE_USER_REF, null);
        }

        return userRef;
    }

    private Intent getUserRefIntent(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(UIConstants.BUNDLE_USER_REF, mViewModel.getUserRef());

        return intent;
    }

    /**
     * Change the toolbar when we go to the event details from the swipe or the opposite
     * @param goDetails, tell if we go to the swipe or the event details
     */
    public void updateToolBarSwipe(boolean goDetails){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(goDetails);

        if (goDetails){
            mBinding.mainToolbar.getMenu().findItem(R.id.main_actionbar_add).setVisible(false);
            mBinding.mainToolbar.getMenu().findItem(R.id.main_actionbar_search).setVisible(false);
            mBinding.mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        else
            setupToolbarNavigation();
    }

}
