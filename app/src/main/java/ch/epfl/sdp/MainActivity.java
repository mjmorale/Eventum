package ch.epfl.sdp;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.event.EventFragment;
import ch.epfl.sdp.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove ActionBar on the Application and create the EventDatabaseBuilder
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (Exception e){}

        setContentView(R.layout.main_activity);

        Uri uri = getIntent().getData();
        EventFragment eventFragment = EventFragment.newInstance("fake", new FirestoreDatabase(FirebaseFirestore.getInstance()));
  
        if(uri!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, eventFragment).commitNow();
        }else 
            if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
