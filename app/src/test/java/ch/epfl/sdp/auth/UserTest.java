package ch.epfl.sdp.auth;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private final static String DUMMY_UID = "123456789abcdef";
    private final static String DUMMY_NAME = "name surname";
    private final static String DUMMY_EMAIL = "name.surname@mail.com";

    private final static String DUMMY_UID2 = "sdkfgjhsdflkgjh2";
    private final static String DUMMY_NAME2 = "name2 surname2";
    private final static String DUMMY_EMAIL2 = "name2.surname2@mail.com";

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfUidIsNull() {
        User user = new User(null, DUMMY_NAME, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfNameIsNull() {
        User user = new User(DUMMY_UID, null, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfEmailIsNull() {
        User user = new User(DUMMY_UID, DUMMY_NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfUidIsEmpty() {
        User user = new User("", DUMMY_NAME, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfNameIsEmpty() {
        User user = new User(DUMMY_UID, "", DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfEmailIsEmpty() {
        User user = new User(DUMMY_UID, DUMMY_NAME, "");
    }

    @Test
    public void user_GetterForUIDReturnsCorrectValue() {
        User user = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertEquals(DUMMY_UID, user.getUid());
    }

    @Test
    public void user_GetterForNameReturnsCorrectValue() {
        User user = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertEquals(DUMMY_NAME, user.getName());
    }

    @Test
    public void user_GetterForEmailReturnsCorrectValue() {
        User user = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertEquals(DUMMY_EMAIL, user.getEmail());
    }

    @Test
    public void user_EqualsReturnsTrueIfSameReference() {
        User user = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertTrue(user.equals(user));
    }

    @Test
    public void user_EqualsReturnsFalseIfNull() {
        User user = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertFalse(user.equals(null));
    }

    @Test
    public void user_EqualsReturnsFalseIfNotSameClass() {
        User user = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertFalse(user.equals(new Object()));
    }

    @Test
    public void user_EqualsReturnsFalseIfUidAreDifferent() {
        User user1 = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);
        User user2 = new User(DUMMY_UID2, DUMMY_NAME, DUMMY_EMAIL);

        assertFalse(user1.equals(user2));
    }

    @Test
    public void user_EqualsReturnsFalseIfNameAreDifferent() {
        User user1 = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);
        User user2 = new User(DUMMY_UID, DUMMY_NAME2, DUMMY_EMAIL);

        assertFalse(user1.equals(user2));
    }

    @Test
    public void user_EqualsReturnsFalseIfEmailAreDifferent() {
        User user1 = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);
        User user2 = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL2);

        assertFalse(user1.equals(user2));
    }

    @Test
    public void user_EqualsReturnsTrueIfTheSame() {
        User user1 = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);
        User user2 = new User(DUMMY_UID, DUMMY_NAME, DUMMY_EMAIL);

        assertTrue(user1.equals(user2));
    }
}
