package com.zee.graphqlcourse.service;

import com.zee.graphqlcourse.codegen.types.CompanyInput;
import com.zee.graphqlcourse.codegen.types.CreationResponse;
import com.zee.graphqlcourse.entity.Company;
import com.zee.graphqlcourse.repository.CompanyRepository;
import com.zee.graphqlcourse.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 29 Aug, 2024
 */


@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;


    public CreationResponse createCompany(CompanyInput input) {
        Company persistedCompany = companyRepository.save(mapperUtil.mapToEntity(input));

        return CreationResponse.newBuilder()
                .uuid(persistedCompany.getUuid().toString())
                .message("Company with name " + persistedCompany.getName() + " created successfully")
                .success(true)
                .build();
    }
}
