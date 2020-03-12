package ch.epfl.sdp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
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
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        if (DatabaseObjectBuilderFactory.getBuilder(Event.class) == null) {
            try {
                DatabaseObjectBuilderFactory.registerBuilder(Event.class, EventDatabaseBuilder.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
