package com.zee.graphqlcourse.api;

import com.zee.graphqlcourse.codegen.DgsConstants;
import com.zee.graphqlcourse.codegen.types.*;
import com.zee.graphqlcourse.service.EmployeeOutsourcedService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;

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

    //using __name for common objects .. interface

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.EmployeeSearchByStaffId
    )
    public Person employeeSearchByStaffId(@Argument("staffId") String id){
        return employeeOutsourcedService.employeeSearchByStaffId(id);
    }

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.EmployeeSearch
    )
    public List<Person> employeeSearch(@Argument("outsourced") Boolean outsourced){
        return employeeOutsourcedService.employeeSearch(outsourced);
    }

    @SchemaMapping(
            typeName = DgsConstants.SUBSCRIPTION_TYPE,
            field = DgsConstants.SUBSCRIPTION.CreatedEmployee
    )
    public Flux<EmployeeDto> createdEmployee(){
        return employeeOutsourcedService.employeeDtoFlux();
    }
}
