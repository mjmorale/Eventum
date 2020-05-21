package ch.epfl.sdp;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a message from the chat
 */
public class ChatMessage {
    private final String mText;
    private final String mUserId;
    private final String mName;
    private Date mDate;

    @SuppressLint("SimpleDateFormat")
    static private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * Constructor of message if date not from firebase
     * @param text Text of the message
     * @param date Date of the message
     * @param uid UID of the creator
     * @param name Name of the creator
     */
    public ChatMessage(@NonNull String text,
                       @Nullable Date date,
                       @NonNull String uid,
                       @NonNull String name) {

        verifyNotNull(text, uid, name);
        mText = text;
        mDate = date;
        mUserId = uid;
        mName = name;
    }

    /**
     * Formats the date using the specified formatter
     * @param date a Date object containing the date at which the message was sent
     * @return a String representation of the date
     */
    static public String formatDate(Date date) {
        return mFormatter.format(date);
    }

    /**
     * Gives the text content of the message
     * @return the text content of the message
     */
    public String getText() {
        return mText;
    }

    /**
     * Gives the date at which the message has been sent
     * @return a Date object of the time at which the message was sent
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * Gives the date as a string
     * @return string representation of the time at which the message was sent
     */
    public String getDateStr() {
        if (mDate == null) //if a new message is created with the device, backend define the date but it's not available right now here
            return formatDate(new Date());
        else
            return formatDate(mDate);
    }

    /**
     * Gives the user id of the message sender
     * @return uid of the message sender
     */
    public String getUid() {
        return mUserId;
    }

    /**
     * Gives the username of the message sender at the time the message was sent
     * @return username of the message sender
     */
    public String getName() {
        return mName;
    }


}