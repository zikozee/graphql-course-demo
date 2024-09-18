package com.zee.graphqlcourse.config;

import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 15 Sep, 2024
 */

@Configuration
public class InstrumentationConfig {

    /**
     *  Adds execution information to response body
     */
    @Bean
    Instrumentation tracingInstrumentation() {
        return new TracingInstrumentation();
    }

    /**
     *  Limits the number of fields a query response body can have
     *  hence restricting caller to certain number of fields
     *  note ** tracingInstrumentation will increase this
     */
    @Bean
    Instrumentation maxQueryComplexityInstrumentation() {
        return new MaxQueryComplexityInstrumentation(20);
    }

    /**
     *  Limits the  depth a query can be. e.g. the below is 7 depth
     *  hence limiting callers to specific number  of depth
     *  query deepSeries {
     *   fetchDepartmentsByCompanyName {
     *     departments {
     *       address {
     *         entityId
     *       }
     *     }
     *   }
     * }
     */
    @Bean
    Instrumentation maxQueryDepthInstrumentation() {
        return new MaxQueryDepthInstrumentation(7);
    }
}
