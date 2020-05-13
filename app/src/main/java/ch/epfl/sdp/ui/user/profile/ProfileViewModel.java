package ch.epfl.sdp.ui.user.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ProfileViewModel extends ViewModel {
    private LiveData <User> mUser;

    static class ProfileViewModelFactory extends DatabaseViewModelFactory{
        ProfileViewModelFactory(){super(String.class);}

        void setUserRef(String userRef){setValue(0, verifyNotNull(userRef));}
    }

    public ProfileViewModel(@NonNull String userRef, @NonNull Database database){
        verifyNotNull(userRef, database);

        mUser = database.query("Users").document(userRef).liveData(User.class);
    }

    /**
     * Gets the User
     * @return The user
     */
    public LiveData<User> getUser() {
        return mUser;
    }
}
