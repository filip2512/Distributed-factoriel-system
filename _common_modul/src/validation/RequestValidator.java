/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import exception.InvalidRequestException;

/**
 * Validates incoming request data. Each module must validate independently - never trust the client.
 */
public final class RequestValidator {

    private static final int MAX_FACTORIAL_NUMBER = 1_000_000;
    private static final int MAX_THREAD_COUNT = 64;
    private static final int MAX_ID_LENGTH = 100;

    private RequestValidator() {
    }

    /**
     * Validates start process parameters.
     */
    public static void validateStartParams(int number, int threadCount) throws InvalidRequestException {
        if (number < 1) {
            throw new InvalidRequestException("number must be >= 1, got: " + number);
        }
        if (number > MAX_FACTORIAL_NUMBER) {
            throw new InvalidRequestException("number must be <= " + MAX_FACTORIAL_NUMBER + ", got: " + number);
        }
        if (threadCount < 1) {
            throw new InvalidRequestException("threads must be >= 1, got: " + threadCount);
        }
        if (threadCount > MAX_THREAD_COUNT) {
            throw new InvalidRequestException("threads must be <= " + MAX_THREAD_COUNT + ", got: " + threadCount);
        }
    }

    /**
     * Validates process id.
     */
    public static void validateProcessId(String id) throws InvalidRequestException {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id must not be null or empty");
        }
        if (id.length() > MAX_ID_LENGTH) {
            throw new InvalidRequestException("id length must be <= " + MAX_ID_LENGTH);
        }
        if (id.contains("\"") || id.contains("\\") || id.contains("\n") || id.contains("\r")) {
            throw new InvalidRequestException("id contains invalid characters");
        }
    }
}
