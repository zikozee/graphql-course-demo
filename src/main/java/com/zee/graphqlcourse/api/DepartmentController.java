package com.zee.graphqlcourse.api;

import com.zee.graphqlcourse.codegen.DgsConstants;
import com.zee.graphqlcourse.codegen.types.CreationResponse;
import com.zee.graphqlcourse.codegen.types.DepartmentInput;
import com.zee.graphqlcourse.codegen.types.DepartmentsResponse;
import com.zee.graphqlcourse.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 29 Aug, 2024
 */

@PreAuthorize("hasAnyAuthority('read','create')")
@Controller
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @SchemaMapping(
            typeName = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.CreateDepartment
    )
    public CreationResponse createDepartment(@Argument(value = "departmentInput") DepartmentInput input) {
        return departmentService.createDepartment(input);
    }

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.FetchDepartmentsByCompanyName
    )
    public DepartmentsResponse fetchDepartmentsByCompanyName(@Argument(value = "companyName") String name) {
        return departmentService.fetchDepartmentsByCompanyName(name);
    }
}
