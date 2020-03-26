package ch.epfl.sdp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class User implements Parcelable {

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

    protected User(Parcel in) {
        this(in.readString(), in.readString(), in.readString());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUid);
        dest.writeString(mName);
        dest.writeString(mEmail);
    }
}
