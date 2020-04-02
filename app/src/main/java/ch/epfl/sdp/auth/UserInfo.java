package ch.epfl.sdp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
