package ch.epfl.sdp.auth;

import android.net.Uri;

import androidx.annotation.Nullable;

public class User {

    private final String mUid;
    private final String mName;
    private final String mEmail;

    public User(String uid, String name, String email) {
        if(uid == null || name == null || email == null) {
            throw new IllegalArgumentException();
        }
        if(uid.isEmpty() || name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.mUid = uid;
        this.mName = name;
        this.mEmail = email;
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
        if(this == obj) {
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        User user = (User)obj;

        return user.mUid == this.mUid && user.mName == this.mName && user.mEmail == this.mEmail;
    }
}
