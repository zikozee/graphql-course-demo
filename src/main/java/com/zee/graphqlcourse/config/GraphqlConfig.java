package com.zee.graphqlcourse.config;

import graphql.scalars.ExtendedScalars;
import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
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

        ValidationSchemaWiring schemaWiring = new ValidationSchemaWiring(
                ValidationRules.newValidationRules()
                        .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                        .build()
        );

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

                .directiveWiring(schemaWiring)

                .build();
    }
}
