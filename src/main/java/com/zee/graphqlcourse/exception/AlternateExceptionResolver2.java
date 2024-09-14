package com.zee.graphqlcourse.exception;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;

import java.util.List;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 24 May, 2024
 */
//alternative to Global Exception
//@Component
public class AlternateExceptionResolver2 extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if(ex instanceof ProcessException){
            return GraphQLError.newError()
                    .message(ex.getLocalizedMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }else if(ex instanceof AuthenticationException){
                return GraphQLError.newError()
                        .message(ex.getLocalizedMessage())
                        .errorType(ErrorType.BAD_REQUEST)
                        .path(env.getExecutionStepInfo().getPath())
                        .location(env.getField().getSourceLocation())
                        .build();

        }

        return  GraphQLError.newError()
                .message(ex.getLocalizedMessage())
                .errorType(ErrorType.INTERNAL_ERROR)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    @Override // Highest Precedence
    protected List<GraphQLError> resolveToMultipleErrors(Throwable ex, DataFetchingEnvironment env) {
        if(ex instanceof ProcessException){
            return List.of(
                    GraphQLError.newError()
                    .message(ex.getLocalizedMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build()
            );
        }else if(ex instanceof AuthenticationException){
            return List.of(
                    GraphQLError.newError()
                            .message(ex.getLocalizedMessage())
                            .errorType(ErrorType.BAD_REQUEST)
                            .path(env.getExecutionStepInfo().getPath())
                            .location(env.getField().getSourceLocation())
                            .build()
            );

        }

        return List.of(
                GraphQLError.newError()
                        .message(ex.getLocalizedMessage())
                        .errorType(ErrorType.INTERNAL_ERROR)
                        .path(env.getExecutionStepInfo().getPath())
                        .location(env.getField().getSourceLocation())
                        .build()
        );
    }
}
