package com.zee.grapqlcourse.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 24 Aug, 2024
 */

@Configuration
public class GraphqlConfig {

    // just add a couple of them and show how to wire it in
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {

        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.NegativeInt)
                .scalar(ExtendedScalars.NonNegativeInt)
                .scalar(ExtendedScalars.NonNegativeFloat)
                .scalar(ExtendedScalars.PositiveInt)
                .scalar(ExtendedScalars.PositiveFloat)
                .scalar(ExtendedScalars.NonPositiveFloat)
                .scalar(ExtendedScalars.GraphQLLong)
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.Currency)
                .build();
    }
}
