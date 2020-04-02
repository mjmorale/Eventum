package ch.epfl.sdp;

import androidx.annotation.Nullable;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class User {

    private final String mName;
    private final String mEmail;

    public User(String name, String email) {
        this.mName = verifyNotNull(name);
        this.mEmail = verifyNotNull(email);
        if(name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException();
        }
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
        return (user.mName.equals(mName) &
                user.mEmail.equals(mEmail));
    }
}
