package ch.epfl.sdp.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class MainViewModel extends ViewModel {

    static class MainViewModelFactory extends DatabaseViewModelFactory {
        MainViewModelFactory() {
            super(String.class);
        }

        void setUserRef(@NonNull String userRef) {
            setValue(0, verifyNotNull(userRef));
        }
    }

    private DocumentQuery mUserDocument;
    private String mUserRef;

    private LiveData<User> mUserLiveData;

    public MainViewModel(@NonNull String userRef, @NonNull Database database) {
        mUserRef = userRef;
        mUserDocument = database.query("users").document(userRef);
    }

    public LiveData<User> getUser() {
        if(mUserLiveData == null) {
            mUserLiveData = mUserDocument.livedata(User.class);
        }
        return mUserLiveData;
    }

    public String getUserRef() {
        return mUserRef;
    }
}