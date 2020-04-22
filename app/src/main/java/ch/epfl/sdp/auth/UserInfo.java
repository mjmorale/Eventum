package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Represents the data that will be queried after an authentication operation.
 * @see Authenticator
 */
public class UserInfo {

    private final String mUid;
    private final String mDisplayName;
    private final String mEmail;

    /**
     * Creates a new instance of UserInfo.
     *
     * @param uid The user's authentication id
     * @param displayName The user's name
     * @param email The user's email address
     * @throws IllegalArgumentException One or more argument is null.
     */
    public UserInfo(@NonNull String uid, @NonNull String displayName, @NonNull String email) {
        mUid = verifyNotNull(uid);
        mDisplayName = verifyNotNull(displayName);
        mEmail = verifyNotNull(email);
    }

    /**
     * @return The user's authentication id.
     */
    public String getUid() {
        return mUid;
    }

    /**
     * @return The user's name.
     */
    public String getDisplayName() {
        return mDisplayName;
    }

    /**
     * @return The user's email address.
     */
    public String getEmail() {
        return mEmail;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;

        final UserInfo user = (UserInfo)obj;
        return (user.mUid.equals(mUid) &
                user.mDisplayName.equals(mDisplayName) &
                user.mEmail.equals(mEmail));
    }
}
