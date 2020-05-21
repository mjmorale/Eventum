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
    private String mImageId;
    private String DEFAULT_IMAGE_URL= "https://firebasestorage.googleapis.com/v0/b/eventum-6a6b7.appspot.com/o/4de42547-d6c4-4451-a9c8-f2750854d0a4?alt=media&token=8a2fec57-918a-4cad-af60-269aaf376846";
    private String mDescription;
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
        this.mImageId=DEFAULT_IMAGE_URL;
        this.mDescription= "Hello there i'm using Eventum!";
        if(name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public User(@NonNull String name, @NonNull String email, @NonNull String imageId, @NonNull String description) {
        this(name, email);
        this.mImageId=verifyNotNull(imageId);
        this.mDescription=verifyNotNull(description);
        if(imageId.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }


    /**
     *
     * @return the user's image id
     */
    public String getImageId(){
        return mImageId;
    }

    /**
     *
     * @return the user's description
     */
    public String getDescription(){
        return mDescription;
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
