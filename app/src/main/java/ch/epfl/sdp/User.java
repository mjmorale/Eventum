package ch.epfl.sdp;

import androidx.annotation.Nullable;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class User {

    private final String mUid;
    private final String mName;
    private final String mEmail;

    public User(String uid, String name, String email) {
        this.mUid = verifyNotNull(uid);
        this.mName = verifyNotNull(name);
        this.mEmail = verifyNotNull(email);
        if(uid.isEmpty() || name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public String getUid() {
        return mUid;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        final User user = (User)obj;
        return (user.mUid.equals(mUid) &
                user.mName.equals(mName) &
                user.mEmail.equals(mEmail));
    }
}
