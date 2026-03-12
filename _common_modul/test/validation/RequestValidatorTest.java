/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import exception.InvalidRequestException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author FILIP KOSTIC
 */
public class RequestValidatorTest {

    @Test
    public void validateStartParams_validParams_doesNotThrow() throws InvalidRequestException {
        RequestValidator.validateStartParams(1, 1);
        RequestValidator.validateStartParams(100, 4);
        RequestValidator.validateStartParams(1_000_000, 64);
    }

    @Test(expected = InvalidRequestException.class)
    public void validateStartParams_numberTooSmall_throws() throws InvalidRequestException {
        RequestValidator.validateStartParams(0, 1);
    }

    @Test(expected = InvalidRequestException.class)
    public void validateStartParams_numberNegative_throws() throws InvalidRequestException {
        RequestValidator.validateStartParams(-1, 1);
    }

    @Test(expected = InvalidRequestException.class)
    public void validateStartParams_numberTooLarge_throws() throws InvalidRequestException {
        RequestValidator.validateStartParams(1_000_001, 1);
    }

    @Test(expected = InvalidRequestException.class)
    public void validateStartParams_threadsTooSmall_throws() throws InvalidRequestException {
        RequestValidator.validateStartParams(1, 0);
    }

    @Test(expected = InvalidRequestException.class)
    public void validateStartParams_threadsTooLarge_throws() throws InvalidRequestException {
        RequestValidator.validateStartParams(1, 65);
    }

    @Test
    public void validateProcessId_validId_doesNotThrow() throws InvalidRequestException {
        RequestValidator.validateProcessId("abc-123");
        RequestValidator.validateProcessId("550e8400-e29b-41d4-a716-446655440000");
    }

    @Test(expected = InvalidRequestException.class)
    public void validateProcessId_null_throws() throws InvalidRequestException {
        RequestValidator.validateProcessId(null);
    }

    @Test(expected = InvalidRequestException.class)
    public void validateProcessId_empty_throws() throws InvalidRequestException {
        RequestValidator.validateProcessId("");
    }

    @Test(expected = InvalidRequestException.class)
    public void validateProcessId_blank_throws() throws InvalidRequestException {
        RequestValidator.validateProcessId("   ");
    }

    @Test(expected = InvalidRequestException.class)
    public void validateProcessId_containsQuote_throws() throws InvalidRequestException {
        RequestValidator.validateProcessId("id\"with\"quote");
    }

    @Test(expected = InvalidRequestException.class)
    public void validateProcessId_containsNewline_throws() throws InvalidRequestException {
        RequestValidator.validateProcessId("id\nwith\nnewline");
    }

    @Test(expected = InvalidRequestException.class)
    public void validateProcessId_tooLong_throws() throws InvalidRequestException {
        RequestValidator.validateProcessId("a".repeat(101));
    }
}
