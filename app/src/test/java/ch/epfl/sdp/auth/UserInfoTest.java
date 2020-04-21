package ch.epfl.sdp.auth;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserInfoTest {

    private final static String DUMMY_UID = "testuid123456";
    private final static String DUMMY_NAME = "name surname";
    private final static String DUMMY_EMAIL = "name.surname@mail.com";

    @Test(expected = IllegalArgumentException.class)
    public void UserInfo_Constructor_FailsIfUidIsNull() {
        UserInfo info = new UserInfo(null, DUMMY_NAME, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void UserInfo_Constructor_FailsIfNameIsNull() {
        UserInfo info = new UserInfo(DUMMY_UID, null, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void UserInfo_Constructor_FailsIfEmailIsNull() {
        UserInfo user = new UserInfo(DUMMY_UID, DUMMY_NAME, null);
    }

    @Test
    public void UserInfo_GetUid_ReturnsCorrectValue() {
        UserInfo info = new UserInfo(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertEquals(DUMMY_UID, info.getUid());
    }

    @Test
    public void UserInfo_GetName_ReturnsCorrectValue() {
        UserInfo info = new UserInfo(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertEquals(DUMMY_NAME, info.getDisplayName());
    }

    @Test
    public void UserInfo_GetEmail_ReturnsCorrectValue() {
        UserInfo info = new UserInfo(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertEquals(DUMMY_EMAIL, info.getEmail());
    }
}
