package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.core.view.GravityCompat;
import ch.epfl.sdp.databinding.MainActivityBinding;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.map.MapFragment;
import ch.epfl.sdp.ui.swipe.SwipeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private MainActivityBinding mBinding;

    private static final int RC_CREATE_EVENT = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = MainActivityBinding.inflate(getLayoutInflater());
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
        getMenuInflater().inflate(R.menu.main_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.main_actionbar_add:
                Intent intent = new Intent(this, CreateEventActivity.class);
                startActivityForResult(intent, RC_CREATE_EVENT);
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

        if(requestCode == RC_CREATE_EVENT) {
            if(resultCode == RESULT_OK) {
                Event event = (Event) data.getExtras().get(CreateEventActivity.CREATE_EVENT_DATA);
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
                Toast.makeText(this, "User profile", Toast.LENGTH_SHORT).show();
                mBinding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
    }
}
