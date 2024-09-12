package com.zee.graphqlcourse.exception;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 22 May, 2024
 */

public class ProcessException extends RuntimeException {

    public ProcessException() {
        super();
    }

    public ProcessException(String message) {
        super(message);
    }
}
