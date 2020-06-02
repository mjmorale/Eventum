package ch.epfl.sdp.ui.user.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ProfileViewModel extends ViewModel {
    private String mUser;
    private LiveData<User> mUserLiveData;
    private Database mDatabase;

    static class ProfileViewModelFactory extends DatabaseViewModelFactory{
        ProfileViewModelFactory(){super(String.class);}

        void setUserRef(String userRef){setValue(0, verifyNotNull(userRef));}
    }

    public ProfileViewModel(@NonNull String userRef, @NonNull Database database){
        verifyNotNull(userRef, database);
        mDatabase = database;
        mUser = userRef;
    }

    /**
     * Gets the User
     * @return The user
     */
    public LiveData<User> getUser() {
        if(mUserLiveData == null){
            mUserLiveData = mDatabase.query("users").document(mUser).liveData(User.class);
        }
        return mUserLiveData;
    }
}
