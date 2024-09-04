package com.zee.graphqlcourse.service;

import com.zee.graphqlcourse.codegen.types.*;
import com.zee.graphqlcourse.entity.Address;
import com.zee.graphqlcourse.entity.Department;
import com.zee.graphqlcourse.repository.DepartmentRepository;
import com.zee.graphqlcourse.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 29 Aug, 2024
 */


@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final AddressService addressService;
    private final MapperUtil mapperUtil;


    public CreationResponse createDepartment(DepartmentInput input) {

        Department persistedDepartment = departmentRepository.save(mapperUtil.mapToDepartmentEntity(input));

        AddressDto addressDto = mapperUtil.mapToAddressDto(input.getAddress());
        Address address = mapperUtil.mapToAddressEntity(addressDto);

        address.setEntityId(persistedDepartment.getDepartmentNo());

        addressService.save(address);

        return CreationResponse.newBuilder()
                .uuid(persistedDepartment.getUuid().toString())
                .message("Department with name " + persistedDepartment.getName() + " created successfully")
                .success(true)
                .build();
    }

    public DepartmentsResponse fetchDepartmentsByCompanyName(String name) {
        List<DepartmentDto> departmentDtos = departmentRepository.findDepartmentByCompanyName(name)
                .stream()
                .map(mapperUtil::mapToDepartmentDto)
                .peek(departmentDto -> {
                    List<AddressDto> addressDtos = addressService.findAddressByEntityId(departmentDto.getDepartmentNo());
                    departmentDto.setAddress(addressDtos.getFirst());
                })
                .toList();


        if(departmentDtos.isEmpty()){
            return DepartmentsResponse.newBuilder()
                    .message("No departments found with companyName " + name)
                    .success(false)
                    .departments(departmentDtos).build();
        }

        return DepartmentsResponse.newBuilder()
                .message("Departments under '" + name + "' retrieved successfully")
                .success(true)
                .departments(departmentDtos)
                .build();
    }

}
