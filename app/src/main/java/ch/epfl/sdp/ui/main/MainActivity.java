package ch.epfl.sdp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.databinding.ActivityMainBinding;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.createevent.CreateEventActivity;
import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.event.EventActivity;
import ch.epfl.sdp.ui.main.attending.AttendingListFragment;
import ch.epfl.sdp.ui.main.map.MapFragment;
import ch.epfl.sdp.ui.main.swipe.SwipeFragment;

import ch.epfl.sdp.ui.settings.SettingsActivity;
import ch.epfl.sdp.ui.user.UserActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;
    private final MainViewModel.MainViewModelFactory mFactory;

    private View mMainNavHeaderView;

    public MainActivity() {
        mFactory = new MainViewModel.MainViewModelFactory();
        mFactory.setDatabase(ServiceProvider.getInstance().getDatabase());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setupToolbarNavigation();

        // Build the view model
        mFactory.setUserRef(getUserRefFromIntent(getIntent()));
        mViewModel = new ViewModelProvider(this, mFactory).get(MainViewModel.class);
        mViewModel.getUser().observe(this, user -> {
            if(user != null) {
                TextView username = mMainNavHeaderView.findViewById(R.id.main_nav_header_username);
                TextView email = mMainNavHeaderView.findViewById(R.id.main_nav_header_email);

                username.setText(user.getName());
                email.setText(user.getEmail());
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new SwipeFragment())
                    .commit();
        }
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
                intent.putExtra(UIConstants.BUNDLE_USER_REF, mViewModel.getUserRef());

                startActivityForResult(intent, UIConstants.RC_CREATE_EVENT);
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
                Intent eventIntent = new Intent(this, EventActivity.class);
                eventIntent.putExtra(UIConstants.BUNDLE_EVENT_MODE_REF, EventActivity.EventActivityMode.ORGANIZER);
                eventIntent.putExtra(UIConstants.BUNDLE_EVENT_REF, eventRef);
                eventIntent.putExtra(UIConstants.BUNDLE_USER_REF, mViewModel.getUserRef());

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
                intent.putExtra(UIConstants.BUNDLE_USER_REF, mViewModel.getUserRef());

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
                intent.putExtra(UIConstants.BUNDLE_USER_REF, mViewModel.getUserRef());

                startActivity(intent);
                break;
        }
    }

    private void setupToolbarNavigation() {
        setSupportActionBar(mBinding.mainToolbar);

        mBinding.mainNavView.setNavigationItemSelectedListener(this);
        mMainNavHeaderView = mBinding.mainNavView.getHeaderView(0).findViewById(R.id.main_nav_header_layout);
        mMainNavHeaderView.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.mainDrawerLayout, mBinding.mainToolbar,
                R.string.navigation_main_drawer_open, R.string.navigation_main_drawer_close);
        mBinding.mainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
}
