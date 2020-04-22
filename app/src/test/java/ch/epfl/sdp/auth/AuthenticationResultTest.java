package ch.epfl.sdp.auth;

import org.junit.Test;

import ch.epfl.sdp.User;

import static org.junit.Assert.*;

public class AuthenticationResultTest {

    private static class AuthenticationTestException extends Exception { }

    private static final UserInfo DUMMY_USERINFO = new UserInfo("fakeuid123456", "name surname", "name.surname@mail.com");
    private static final AuthenticationTestException DUMMY_EXCEPTION = new AuthenticationTestException();

    @Test(expected = IllegalArgumentException.class)
    public void authenticationResult_successFailsIfUserIsNull() {
        AuthenticationResult authenticationResult = AuthenticationResult.success(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void authenticationResult_failureFailsIfExceptionIsNull() {
        AuthenticationResult authenticationResult = AuthenticationResult.failure(null);
    }

    @Test
    public void authenticationResult_successReturnsTrueOnIsSuccessful() {
        AuthenticationResult authenticationResult = AuthenticationResult.success(DUMMY_USERINFO);

        assertTrue(authenticationResult.isSuccessful());
    }

    @Test
    public void authenticationResult_failureReturnsFalseOnIsSuccessful() {
        AuthenticationResult authenticationResult = AuthenticationResult.failure(DUMMY_EXCEPTION);

        assertFalse(authenticationResult.isSuccessful());
    }

    @Test
    public void authenticationResult_successReturnsTheCorrectUser() {
        AuthenticationResult authenticationResult = AuthenticationResult.success(DUMMY_USERINFO);

        assertEquals(DUMMY_USERINFO, authenticationResult.getUserInfo());
    }

    @Test
    public void authenticationResult_failureReturnsTheCorrectException() {
        AuthenticationResult authenticationResult = AuthenticationResult.failure(DUMMY_EXCEPTION);

        assertEquals(DUMMY_EXCEPTION, authenticationResult.getException());
    }
}
