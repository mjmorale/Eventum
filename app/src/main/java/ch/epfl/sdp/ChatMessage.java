package ch.epfl.sdp;

import androidx.annotation.NonNull;


import static ch.epfl.sdp.ObjectUtils.verifyNotNull;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ChatMessage {
    private final String mText;
    private final Date mDate;
    private final String mUserId;
    private final String mName;

    static private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public ChatMessage(@NonNull String text,
                       @NonNull Date date,
                       @NonNull String uid,
                       @NonNull String name) {

        verifyNotNull(text, date, uid, name);

        mText = text;
        mDate = date;
        mUserId = uid;
        mName = name;
    }

    static public String formatDate(Date date) {
        return mFormatter.format(date);
    }

    public String getText() {
        return mText;
    }

    public Date getDate() {
        return mDate;
    }

    public String getDateStr() {
        return formatDate(mDate);
    }

    public String getUid() {
        return mUserId;
    }

    public String getName() {
        return mName;
    }


}
