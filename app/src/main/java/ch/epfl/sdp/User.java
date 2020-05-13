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
        this.mImageId= "";
        this.mDescription="";
        if(name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public User(@NonNull String name, @NonNull String email, @NonNull String imageId, @NonNull String description) {
        this(name, email);
        this.mImageId=verifyNotNull(imageId);
        this.mDescription=verifyNotNull(description);
    }

    public void setImage(@NonNull String imageId){
        this.mImageId= verifyNotNull(imageId);
    }

    public void setDescription(String description){
        this.mDescription = verifyNotNull(description);
    }

    public String getImageId(){
        return mImageId;
    }
    
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
