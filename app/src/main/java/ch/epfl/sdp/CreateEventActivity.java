package ch.epfl.sdp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import ch.epfl.sdp.databinding.CreateEventActivityBinding;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CREATE_EVENT_DATA = "create_event_data";

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
                String title = mBinding.title.getText().toString();
                String description = mBinding.description.getText().toString();
                String date = mBinding.date.getDayOfMonth() + "/" + mBinding.date.getMonth() + "/" + mBinding.date.getYear();

                try {
                    checkInput(title, description, date);
                    mViewModel.insertEvent(title, description, date, new CreateEventViewModel.OnEventCreatedCallback() {
                        @Override
                        public void onSuccess(Event event) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(CREATE_EVENT_DATA, event);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                    });
                } catch (ParseException e) {
                    Toast.makeText(this,
                            "Invalid date", Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(this,
                            "Invalid input", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
