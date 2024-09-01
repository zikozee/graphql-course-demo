package com.zee.graphqlcourse.service;

import com.zee.graphqlcourse.codegen.types.*;
import com.zee.graphqlcourse.entity.Address;
import com.zee.graphqlcourse.entity.Employee;
import com.zee.graphqlcourse.entity.Outsourced;
import com.zee.graphqlcourse.repository.AddressRepository;
import com.zee.graphqlcourse.repository.EmployeeRepository;
import com.zee.graphqlcourse.repository.OutsourcedRepository;
import com.zee.graphqlcourse.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


/**
 * @author : Ezekiel Eromosei
 * @code @created : 29 Aug, 2024
 */

@Service
@RequiredArgsConstructor
public class EmployeeOutsourcedService {

    private final EmployeeRepository employeeRepository;
    private final OutsourcedRepository outsourcedRepository;
    private final AddressRepository addressRepository;
    private final MapperUtil mapperUtil;


    public CreationResponse createEmployeeOutsourced(EmployeeOutsourcedInput input) {
        return (!StringUtils.hasText(input.getOutsourceId()) && input.getDuty() == null)
                ? createEmployee(input) : createOutsourced(input);
    }

    private CreationResponse createEmployee(EmployeeOutsourcedInput input) {
        if(!StringUtils.hasText(input.getEmployeeId()) || !StringUtils.hasText(input.getDepartmentNo())
            || !StringUtils.hasText(input.getEmail()) || input.getRole() == null) {
            throw new RuntimeException("employeeId, departmentNo, email, role is required");
        }

        EmployeeDto employeeDto = mapperUtil.mapToDto(input);
        Employee persisedEmployee = employeeRepository.save(mapperUtil.mapToEntity(employeeDto));

        List<Address> addressList = input.getAddress().stream()
                .map(mapperUtil::mapToDto)
                .map(dto -> {
                    Address address = mapperUtil.mapToEntity(dto);
                    address.setEntityId(persisedEmployee.getEmployeeId());
                    return address;
                })
                .toList();

        addressRepository.saveAll(addressList);

        return CreationResponse.newBuilder()
                .uuid(persisedEmployee.getUuid().toString())
                .message("Employee with name " + persisedEmployee.getName() + " created successfully")
                .success(true)
                .build();
    }

    private CreationResponse createOutsourced(EmployeeOutsourcedInput input) {
        OutsourcedDto outsourcedDto = mapperUtil.mapToOutsourcedDto(input);
        Outsourced persistedOutsourced = outsourcedRepository.save(mapperUtil.mapToEntity(outsourcedDto));

        List<Address> addressList = input.getAddress().stream()
                .map(mapperUtil::mapToDto)
                .map(dto -> {
                    Address address = mapperUtil.mapToEntity(dto);
                    address.setEntityId(persistedOutsourced.getOutsourceId());
                    return address;
                })
                .toList();

        addressRepository.saveAll(addressList);

        return CreationResponse.newBuilder()
                .uuid(persistedOutsourced.getUuid().toString())
                .message("Outsourced with name " + persistedOutsourced.getName() + " created successfully")
                .success(true)
                .build();
    }


    public CreationResponse updateExistingEmployeeAddress(AddressInput input) {

        Optional<Address> optionalAddress = addressRepository.findByEntityId(input.getEntityId());

        Address address = optionalAddress
                .orElseThrow(() -> new RuntimeException("address for employee with id: '" + input.getEntityId() + "'not found"));

        //todo assume no input is null for now: we will do validation later
        // change to specification later
        if(input.getEntityId().trim().equalsIgnoreCase(address.getEntityId())
                && input.getStreet().trim().equalsIgnoreCase(address.getStreet())
                && input.getCity().trim().equalsIgnoreCase(address.getCity())
                && input.getState().trim().equalsIgnoreCase(address.getState())
                && input.getZipCode() == address.getZipCode()){
            throw new RuntimeException("address for employee with id: '" + input.getEntityId() +"'already exist");
        }

        AddressDto addressDto = mapperUtil.mapToDto(input);

        Address newAddress = mapperUtil.mapToEntity(addressDto);
        newAddress.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        Address persistedAddress = addressRepository.save(newAddress);

        return CreationResponse.newBuilder()
                .uuid(persistedAddress.getUuid().toString())
                .message("Employee Address updated successfully")
                .success(true)
                .build();
    }

    public CreationResponse updateEmployeeDetails(EmployeeUpdateInput input) {

        Employee employee = employeeRepository.findByEmployeeId(input.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("employee with id: '" + input.getEmployeeId() + "'not found"));


        employee.setSalary(input.getSalary());
        employee.setAge(input.getAge());
        employee.setRole(input.getRole());
        employee.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        Employee updatedEmployee = employeeRepository.save(employee);

        return CreationResponse.newBuilder()
                .uuid(updatedEmployee.getUuid().toString())
                .success(true)
                .message("Employee updated successfully")
                .build();
    }
}
