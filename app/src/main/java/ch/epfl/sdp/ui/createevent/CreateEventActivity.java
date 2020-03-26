package ch.epfl.sdp.ui.createevent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.CreateEventActivityBinding;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.ui.FirestoreDatabaseViewModelFactory;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EVENT_REF_EXTRA = "event_ref_extra";

    private CreateEventActivityBinding mBinding;
    private CreateEventViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = CreateEventActivityBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.createButton.setOnClickListener(this);

        setSupportActionBar(mBinding.createEventToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        DatabaseViewModelFactory factory = FirestoreDatabaseViewModelFactory.getInstance();
        mViewModel = new ViewModelProvider(this, factory).get(CreateEventViewModel.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    private void checkInput(String title, String description, String date) throws IllegalArgumentException {
        if (title == null || description == null || date == null) {
            throw new IllegalArgumentException();
        }
        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createButton:
                try {
                    tryCreateEvent(new CreateEventViewModel.OnEventCreatedCallback() {
                        @Override
                        public void onSuccess(String eventRef) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(EVENT_REF_EXTRA, eventRef);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                    });
                } catch (ParseException | IllegalArgumentException e) {
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void tryCreateEvent(@NonNull CreateEventViewModel.OnEventCreatedCallback callback) throws ParseException {
        String title = mBinding.title.getText().toString();
        String description = mBinding.description.getText().toString();
        String date = mBinding.date.getDayOfMonth() + "/" + mBinding.date.getMonth() + "/" + mBinding.date.getYear();
        checkInput(title, description, date);

        mViewModel.insertEvent(title, description, date, callback);
    }
}
