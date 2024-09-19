package com.zee.graphqlcourse.api;

import com.zee.graphqlcourse.codegen.DgsConstants;
import com.zee.graphqlcourse.codegen.types.AllCompanyResponse;
import com.zee.graphqlcourse.codegen.types.CompanyDto;
import com.zee.graphqlcourse.codegen.types.CompanyInput;
import com.zee.graphqlcourse.codegen.types.CreationResponse;
import com.zee.graphqlcourse.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 29 Aug, 2024
 */

@PreAuthorize("hasAnyAuthority('read','create')")
@Controller
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @SchemaMapping(
            typeName = DgsConstants.MUTATION.TYPE_NAME,
            field = DgsConstants.MUTATION.CreateCompany
    )
    public CreationResponse createCompany(@Argument(value = "companyInput") CompanyInput input) {
        return companyService.createCompany(input);
    }

    @SchemaMapping(
            typeName = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.FetchAllCompany
    )
    public AllCompanyResponse fetchAllCompany() {
        return companyService.fetchAllCompany();
    }

    @SchemaMapping(
            typeName = DgsConstants.SUBSCRIPTION_TYPE,
            field = DgsConstants.SUBSCRIPTION.CreatedCompany
    )
    public Flux<CompanyDto> createdCompany(){
        return companyService.companyDtoFlux();
    }
}
