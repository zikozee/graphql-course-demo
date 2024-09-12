package com.zee.graphqlcourse.exception;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 22 May, 2024
 */

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("You are not allowed to access this resource");
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
