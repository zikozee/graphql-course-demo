package com.zee.graphqlcourse.service;

import com.zee.graphqlcourse.codegen.types.CompanyDto;
import com.zee.graphqlcourse.codegen.types.EmployeeDto;
import com.zee.graphqlcourse.codegen.types.PersonAndEntitySearch;
import com.zee.graphqlcourse.entity.Company;
import com.zee.graphqlcourse.entity.Employee;
import com.zee.graphqlcourse.repository.CompanyRepository;
import com.zee.graphqlcourse.repository.EmployeeRepository;
import com.zee.graphqlcourse.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 05 Sep, 2024
 */

@Service
@RequiredArgsConstructor
public class CommonService {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final AddressService addressService;
    private final MapperUtil mapperUtil;


    public List<PersonAndEntitySearch> employeeWithCompanySearch(String id) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmployeeId(id);
        if(optionalEmployee.isEmpty()) throw new RuntimeException("Employee not found");



        EmployeeDto employeeDto = optionalEmployee.map(mapperUtil::mapToEmployeeDto).get();
        employeeDto.setAddress(addressService.findAddressByEntityId(employeeDto.getEmployeeId()));

        Company foundCompany = companyRepository.findByName(employeeDto.getCompanyName())
                .orElseThrow(() ->  new RuntimeException("Company not found"));
        CompanyDto companyDto = mapperUtil.mapToCompanyDto(foundCompany);

        return List.of(employeeDto, companyDto);
    }
}
