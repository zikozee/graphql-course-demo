package com.zee.graphqlcourse.api;

import com.zee.graphqlcourse.codegen.DgsConstants;
import com.zee.graphqlcourse.codegen.types.EmployeeDto;
import com.zee.graphqlcourse.codegen.types.OutsourcedDto;
import com.zee.graphqlcourse.codegen.types.PersonAndEntitySearch;
import com.zee.graphqlcourse.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 05 Sep, 2024
 */

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.EmployeeWithCompanySearch
    )
    public List<PersonAndEntitySearch> employeeWithCompanySearch(@Argument("employeeId") String id){
        return commonService.employeeWithCompanySearch(id);
    }

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.FetchEmployeesUsingHeaders
    )
    public List<EmployeeDto> fetchEmployeesUsingHeaders(
            @ContextValue(name="header1", required = false) String header1,
            @ContextValue(value="header2", required = true) String header2,
            @ContextValue(name="header3", required = false) String header3
    ){

        // note you can use in combination with any query or mutation
        return commonService.fetchEmployeesUsingHeaders(header1, header2, header3);
    }

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.FetchOutsourcedUsingHeaders
    )
    public List<OutsourcedDto> fetchOutsourcedUsingHeaders(
            @ContextValue(name="header4", required = false) String header4,
            @ContextValue(value="header5", required = true) String header5
    ){

        // note you can use in combination with any query or mutation
        return commonService.fetchOutsourcedUsingHeaders(header4, header5);
    }
}
