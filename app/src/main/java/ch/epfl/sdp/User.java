package ch.epfl.sdp;

public class User {

    private final String mUid;
    private final String mName;
    private final String mEmail;

    public User(String uid, String name, String email) {
        if(uid == null || name == null || email == null) {
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
}
