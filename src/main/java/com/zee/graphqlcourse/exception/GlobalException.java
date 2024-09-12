package com.zee.graphqlcourse.exception;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
//import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 22 May, 2024
 */
// not you could do a json format to return an object since it expects a string
@ControllerAdvice
public class GlobalException {

    @GraphQlExceptionHandler(ProcessException.class)
    public GraphQLError exception(final ProcessException exception, final DataFetchingEnvironment env) {

        return GraphQLError.newError()
                .message(exception.getLocalizedMessage())
                .errorType(ErrorClassification.errorClassification(ErrorType.BAD_REQUEST.name()))
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    @GraphQlExceptionHandler(AuthenticationException.class)
    public GraphQLError exception(final AuthenticationException exception, final DataFetchingEnvironment env) {

        return GraphQLError.newError()
                .message(exception.getLocalizedMessage())
                .errorType(ErrorClassification.errorClassification(ErrorType.UNAUTHORIZED.name()))
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    @GraphQlExceptionHandler(AccessDeniedException.class)
    public GraphQLError exception(final AccessDeniedException exception, final DataFetchingEnvironment env) {

        return GraphQLError.newError()
                .message(exception.getLocalizedMessage())
                .errorType(ErrorClassification.errorClassification(ErrorType.UNAUTHORIZED.name()))
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

//    @GraphQlExceptionHandler(AuthorizationDeniedException.class)
//    public GraphQLError exception(final AuthorizationDeniedException exception, final DataFetchingEnvironment env) {
//
//        return GraphQLError.newError()
//                .message(exception.getLocalizedMessage())
//                .errorType(ErrorClassification.errorClassification(ErrorType.UNAUTHORIZED.name()))
//                .path(env.getExecutionStepInfo().getPath())
//                .location(env.getField().getSourceLocation())
//                .build();
//    }

    @GraphQlExceptionHandler(NotFoundException.class)
    public GraphQLError exception(final NotFoundException exception, final DataFetchingEnvironment env) {

        return GraphQLError.newError()
                .message(exception.getLocalizedMessage())
                .errorType(ErrorClassification.errorClassification(ErrorType.NOT_FOUND.name()))
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    @GraphQlExceptionHandler(RuntimeException.class)
    public GraphQLError exception(final RuntimeException exception, final DataFetchingEnvironment env) {
        return GraphQLError.newError()
                .message(exception.getLocalizedMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }
}
