package ch.epfl.sdp;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private final static String DUMMY_NAME = "name surname";
    private final static String DUMMY_EMAIL = "name.surname@mail.com";

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfNameIsNull() {
        User user = new User(null, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfEmailIsNull() {
        User user = new User(DUMMY_NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfNameIsEmpty() {
        User user = new User("", DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_ConstructorFailsIfEmailIsEmpty() {
        User user = new User(DUMMY_NAME, "");
    }

    @Test
    public void user_GetterForNameReturnsCorrectValue() {
        User user = new User(DUMMY_NAME, DUMMY_EMAIL);

        assertEquals(DUMMY_NAME, user.getName());
    }

    @Test
    public void user_GetterForEmailReturnsCorrectValue() {
        User user = new User(DUMMY_NAME, DUMMY_EMAIL);

        assertEquals(DUMMY_EMAIL, user.getEmail());
    }
}
