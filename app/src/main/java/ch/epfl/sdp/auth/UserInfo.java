package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class UserInfo {

    private final String mUid;
    private final String mDisplayName;
    private final String mEmail;

    public UserInfo(@NonNull String uid, @NonNull String displayName, @NonNull String email) {
        mUid = verifyNotNull(uid);
        mDisplayName = verifyNotNull(displayName);
        mEmail = verifyNotNull(email);
    }

    public String getUid() {
        return mUid;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getEmail() {
        return mEmail;
    }
}
