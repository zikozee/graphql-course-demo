package com.zee.graphqlcourse.api;

import com.zee.graphqlcourse.codegen.DgsConstants;
import com.zee.graphqlcourse.codegen.types.EmployeePagination;
import com.zee.graphqlcourse.codegen.types.EmployeeSearchInput;
import com.zee.graphqlcourse.codegen.types.OutsourcedSearchInput;
import com.zee.graphqlcourse.entity.Outsourced;
import com.zee.graphqlcourse.service.EmployeeOutsourcedService;
import graphql.relay.Connection;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 09 Sep, 2024
 */

@Controller
@RequiredArgsConstructor
public class PaginationController {

    private final EmployeeOutsourcedService employeeOutsourcedService;

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.EmployeePagination
    )
    public EmployeePagination employeePagination(
            DataFetchingEnvironment dataFetchingEnvironment,
            @Argument(value = "search") Optional<EmployeeSearchInput> inputSearch,
            @Argument("page") Integer page, @Argument("size") Integer size) {

        return employeeOutsourcedService.employeeSearch(inputSearch, page, size, dataFetchingEnvironment);
    }

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.OutsourcedPagination
    )
    public Connection<Outsourced> outsourcedPagination(
            @Argument(value = "search") Optional<OutsourcedSearchInput> input,
            DataFetchingEnvironment dataFetchingEnvironment,
            @Argument Integer first,
            @Argument Integer last,
            @Argument String before,
            @Argument String after) {

        return employeeOutsourcedService.outsourcedSearch(input, dataFetchingEnvironment);
    }
}
