package ch.epfl.sdp;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sdp.databinding.CreateEventActivityBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.event.CreateEventFragment;

public class CreateEventActivity extends AppCompatActivity {

    private CreateEventActivityBinding mBinding;
    private Database mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = CreateEventActivityBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.createEventToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mDb = new FirestoreDatabase(FirebaseFirestore.getInstance());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(mBinding.createEventFragmentContainer.getId(), new CreateEventFragment(mDb))
                    .commit();
        }
    }

}
