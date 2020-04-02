package ch.epfl.sdp.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.databinding.ActivityMainBinding;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.createevent.CreateEventActivity;
import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.createevent.CreateEventFragment;
import ch.epfl.sdp.ui.event.DefaultEventFragment;
import ch.epfl.sdp.ui.event.EventActivity;
import ch.epfl.sdp.ui.main.attending.AttendingListFragment;
import ch.epfl.sdp.ui.main.swipe.EventDetailFragment;
import ch.epfl.sdp.ui.main.swipe.SwipeFragment;
import ch.epfl.sdp.ui.settings.SettingsActivity;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.main.map.MapFragment;
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




        Uri uri = getIntent().getData();
        if(uri!=null){
            List<String> params= uri.getPathSegments();
            int imageID = Integer.parseInt(params.get(params.size()-1));
            Event event = new EventBuilder().setTitle("Title").setDescription("Description").setDate(new Date()).setImageId(imageID).build();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new EventDetailFragment(event, new SwipeFragment()))
                    .commit();
        }else if (savedInstanceState == null) {
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
                        .replace(mBinding.mainContainer.getId(), new MapFragment(new FirestoreDatabase(FirebaseFirestore.getInstance()))).commit();
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
