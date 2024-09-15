package com.zee.graphqlcourse.config;

import com.zee.graphqlcourse.codegen.DgsConstants;
import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.fieldvalidation.FieldAndArguments;
import graphql.execution.instrumentation.fieldvalidation.FieldValidationEnvironment;
import graphql.execution.instrumentation.fieldvalidation.FieldValidationInstrumentation;
import graphql.execution.instrumentation.fieldvalidation.SimpleFieldValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 15 Sep, 2024
 */

@Slf4j
@Configuration
public class CustomInstrumentationConfig {

    // this is quite useful to catch some system defined rules to follow on inputs or queries

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> paginationAgeSearchGreaterThan17Rule(){
        return ((fieldAndArguments, fieldValidationEnvironment) -> {

            Map<String, Object> argEmployee =
                    fieldAndArguments.getArgumentValue(DgsConstants.QUERY.EMPLOYEEPAGINATION_INPUT_ARGUMENT.Search);

            LocalDate dobStart = (LocalDate) argEmployee.getOrDefault(DgsConstants.EMPLOYEESEARCHINPUT.DobStart, LocalDate.now());
            LocalDate dobEnd = (LocalDate) argEmployee.getOrDefault(DgsConstants.EMPLOYEESEARCHINPUT.DobEnd, LocalDate.now());

            int ageA = LocalDate.now().getYear() - dobStart.getYear();
            int ageB = LocalDate.now().getYear() - dobEnd.getYear();

            return ageA < 18 || ageB < 18
                    ? Optional.of(fieldValidationEnvironment.mkError("Age search for start and end must be greater or equal to 18"))
                    : Optional.empty();
        });
    }

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> salaryBelow50000Rule(){
        return ((fieldAndArguments, fieldValidationEnvironment) -> {

            Map<String, Object> argEmployee =
                    fieldAndArguments.getArgumentValue(DgsConstants.MUTATION.CREATEEMPLOYEE_INPUT_ARGUMENT.EmployeeOutsourced);

            BigDecimal salary = (BigDecimal) argEmployee.getOrDefault(DgsConstants.EMPLOYEEOUTSOURCEDINPUT.Salary, 0);

            return salary.compareTo(BigDecimal.valueOf(50000)) <= 0
                    ? Optional.of(fieldValidationEnvironment.mkError("Salary must be above 50000"))
                    : Optional.empty();
        });
    }

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> age65In5yearsRule(){
        return ((fieldAndArguments, fieldValidationEnvironment) -> {

            Map<String, Object> argEmployee =
                    fieldAndArguments.getArgumentValue(DgsConstants.MUTATION.CREATEEMPLOYEE_INPUT_ARGUMENT.EmployeeOutsourced);

            int age = (Integer) argEmployee.getOrDefault(DgsConstants.EMPLOYEEOUTSOURCEDINPUT.Age, 0);

            return age +5 >=65
                    ? Optional.of(fieldValidationEnvironment.mkError("You must not be 65 in 5 years time"))
                    : Optional.empty();
        });
    }

    @Bean
    Instrumentation paginationAgeInstrumentation(){
        ResultPath paginateEmployee = ResultPath.parse("/" + DgsConstants.QUERY.EmployeePagination);

        SimpleFieldValidation validation = new SimpleFieldValidation();
        validation.addRule(paginateEmployee, paginationAgeSearchGreaterThan17Rule());

        return new FieldValidationInstrumentation(validation);
    }

    @Bean
    Instrumentation retirementAgeValidationInstrumentation(){
        ResultPath pathCreateEmployee = ResultPath.parse("/" + DgsConstants.MUTATION.CreateEmployee);

        SimpleFieldValidation validation = new SimpleFieldValidation();
        validation.addRule(pathCreateEmployee, age65In5yearsRule());

        return new FieldValidationInstrumentation(validation);
    }

    @Bean
    Instrumentation salaryBaseValidationInstrumentation(){
        ResultPath pathCreateEmployee = ResultPath.parse("/" + DgsConstants.MUTATION.CreateEmployee);

        SimpleFieldValidation validation = new SimpleFieldValidation();
        validation.addRule(pathCreateEmployee, salaryBelow50000Rule());

        return new FieldValidationInstrumentation(validation);
    }
}
