package com.zee.graphqlcourse.api;

import com.zee.graphqlcourse.codegen.DgsConstants;
import com.zee.graphqlcourse.codegen.types.*;
import com.zee.graphqlcourse.service.EmployeeOutsourcedService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 29 Aug, 2024
 */

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeOutsourcedService employeeOutsourcedService;

    @SchemaMapping(
            typeName = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.CreateEmployee
    )
    public CreationResponse createEmployee(@Argument(value = "employeeOutsourced") EmployeeOutsourcedInput input) {
        return employeeOutsourcedService.createEmployeeOutsourced(input);
    }

    @SchemaMapping(
            typeName = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.UpdateExistingEmployeeAddress
    )
    public CreationResponse updateExistingEmployeeAddress(@Argument(value = "addressInput") AddressInput input) {
        return employeeOutsourcedService.updateExistingEmployeeAddress(input);
    }

//    @SchemaMapping(
//            typeName = DgsConstants.MUTATION.TYPE_NAME,
//            field = DgsConstants.MUTATION.UpdateEmployeeDetails
//    )
    @MutationMapping(value = DgsConstants.MUTATION.UpdateEmployeeDetails)
    public CreationResponse updateEmployeeDetails(@Argument(value = "employeeUpdate") EmployeeUpdateInput input) {
        return employeeOutsourcedService.updateEmployeeDetails(input);
    }
}
