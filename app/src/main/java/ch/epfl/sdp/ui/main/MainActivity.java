package ch.epfl.sdp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.ActivityMainBinding;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.createevent.CreateEventActivity;
import ch.epfl.sdp.ui.event.EventActivity;
import ch.epfl.sdp.ui.main.attending.AttendingListFragment;
import ch.epfl.sdp.ui.main.map.MapFragment;
import ch.epfl.sdp.ui.main.swipe.SwipeFragment;

import ch.epfl.sdp.ui.settings.SettingsActivity;
import ch.epfl.sdp.ui.user.UserActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.mainToolbar);

        mBinding.mainNavView.setNavigationItemSelectedListener(this);
        mBinding.mainNavView.getHeaderView(0).findViewById(R.id.main_nav_header_layout).setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.mainDrawerLayout, mBinding.mainToolbar,
                R.string.navigation_main_drawer_open, R.string.navigation_main_drawer_close);
        mBinding.mainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
