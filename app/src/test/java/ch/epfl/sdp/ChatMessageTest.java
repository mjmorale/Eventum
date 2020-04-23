package ch.epfl.sdp;


import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;


public class ChatMessageTest {

    String text = "text";
    Date date = new Date();
    String uid = "uid";
    String name ="name";
    ChatMessage chatMessage = new ChatMessage(text, date, uid, name);

    @Test
    public void chatMessageTest() {

        assertEquals(chatMessage.getDate(), date);
        assertEquals(chatMessage.getName(), name);
        assertEquals(chatMessage.getText(), text);
        assertEquals(chatMessage.getUid(), uid);
        assertEquals(chatMessage.getDateStr(), ChatMessage.formatDate(date));
    }

}