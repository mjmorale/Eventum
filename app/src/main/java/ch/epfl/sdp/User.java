package ch.epfl.sdp;

import androidx.annotation.NonNull;
import ch.epfl.sdp.auth.UserInfo;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Class that represents a user. This immutable object contains all the data
 * regarding users in the application. It does not contain any database specific values
 * like the authentication id.
 * @see UserInfo
 */
public class User {

    private final String mName;
    private final String mEmail;

    /**
     * Construct a new User instance
     *
     * @param name The name of the user
     * @param email The email address of the user
     * @throws IllegalArgumentException One of the parameter is null or is equal to the empty string.
     */
    public User(@NonNull String name, @NonNull String email) {
        this.mName = verifyNotNull(name);
        this.mEmail = verifyNotNull(email);
        if(name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return The user's name
     */
    public String getName() {
        return mName;
    }

    /**
     * @return The user's email address
     */
    public String getEmail() {
        return mEmail;
    }
}
