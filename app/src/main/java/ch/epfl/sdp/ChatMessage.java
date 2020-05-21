package ch.epfl.sdp;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Represents a message from the chat
 */
public class ChatMessage {
    @SuppressLint("SimpleDateFormat")
    static private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private final String mText;
    private final Date mDate;
    private final String mUserId;
    private final String mName;

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

    /**
     * Formats the date using the specified formatter
     *
     * @param date a Date object containing the date at which the message was sent
     * @return a String representation of the date
     */
    static public String formatDate(Date date) {
        return mFormatter.format(date);
    }

    /**
     * Gives the text content of the message
     *
     * @return the text content of the message
     */
    public String getText() {
        return mText;
    }

    /**
     * Gives the date at which the message has been sent
     *
     * @return a Date object of the time at which the message was sent
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * Gives the date as a string
     *
     * @return string representation of the time at which the message was sent
     */
    public String getDateStr() {
        return formatDate(mDate);
    }

    /**
     * Gives the user id of the message sender
     *
     * @return uid of the message sender
     */
    public String getUid() {
        return mUserId;
    }

    /**
     * Gives the username of the message sender at the time the message was sent
     *
     * @return username of the message sender
     */
    public String getName() {
        return mName;
    }


}
