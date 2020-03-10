package ch.epfl.sdp.db.queries;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class QueryResultTest {

    @Test (expected = IllegalArgumentException.class)
    public void QueryResult_success_FailsIfNullValue() {
        QueryResult.success(null);
    }

    @Test
    public void QueryResult_success_DataIsCorrectlySet() {
        String testData = "test";
        QueryResult<String> res = QueryResult.success(testData);
        assertEquals(testData, res.getData());
    }

    @Test
    public void QueryResult_success_SuccessReturnsSuccessful() {
        String testData = "test";
        QueryResult<String> res = QueryResult.success(testData);
        assertTrue(res.isSuccessful());
    }

    @Test
    public void QueryResult_success_SuccessReturnsNullException() {
        String testData = "test";
        QueryResult<String> res = QueryResult.success(testData);
        assertNull(res.getException());
    }

    @Test (expected = IllegalArgumentException.class)
    public void QueryResult_failure_FailsIfNullValue() {
        QueryResult.failure(null);
    }

    @Test
    public void QueryResult_failure_ExceptionIsCorrectlySet() {
        Exception e = new Exception();
        QueryResult<String> res = QueryResult.failure(e);
        assertEquals(e, res.getException());
    }

    @Test
    public void QueryResult_failure_FailureReturnsNotSuccessful() {
        Exception e = new Exception();
        QueryResult<String> res = QueryResult.failure(e);
        assertFalse(res.isSuccessful());
    }

    @Test
    public void QueryResult_failure_FailureReturnsNullData() {
        Exception e = new Exception();
        QueryResult<String> res = QueryResult.failure(e);
        assertNull(res.getData());
    }
}
