package com.zee.graphqlcourse.service;

import com.zee.graphqlcourse.codegen.types.AddressDto;
import com.zee.graphqlcourse.codegen.types.CreationResponse;
import com.zee.graphqlcourse.codegen.types.DepartmentInput;
import com.zee.graphqlcourse.entity.Address;
import com.zee.graphqlcourse.entity.Department;
import com.zee.graphqlcourse.repository.AddressRepository;
import com.zee.graphqlcourse.repository.DepartmentRepository;
import com.zee.graphqlcourse.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 29 Aug, 2024
 */


@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final AddressRepository addressRepository;
    private final MapperUtil mapperUtil;


    public CreationResponse createDepartment(DepartmentInput input) {

        Department persistedDepartment = departmentRepository.save(mapperUtil.mapToEntity(input));

        AddressDto addressDto = mapperUtil.mapToDto(input.getAddress());
        Address address = mapperUtil.mapToEntity(addressDto);

        address.setEntityId(persistedDepartment.getDepartmentNo());

        addressRepository.save(address);

        return CreationResponse.newBuilder()
                .uuid(persistedDepartment.getUuid().toString())
                .message("Department with name " + persistedDepartment.getName() + " created successfully")
                .success(true)
                .build();
    }
}
