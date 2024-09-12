package com.zee.graphqlcourse.service;

import com.zee.graphqlcourse.codegen.types.*;
import com.zee.graphqlcourse.entity.Address;
import com.zee.graphqlcourse.entity.Employee;
import com.zee.graphqlcourse.entity.Outsourced;
import com.zee.graphqlcourse.exception.NotFoundException;
import com.zee.graphqlcourse.exception.ProcessException;
import com.zee.graphqlcourse.repository.EmployeeRepository;
import com.zee.graphqlcourse.repository.OutsourcedRepository;
import com.zee.graphqlcourse.search.SpecUtil;
import com.zee.graphqlcourse.search.employee.EmployeeSearchQuery;
import com.zee.graphqlcourse.search.outsourced.OutsourcedSearchQuery;
import com.zee.graphqlcourse.util.MapperUtil;
import graphql.relay.Connection;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.zee.graphqlcourse.search.employee.EmployeeSearchQuery.EMPLOYEE_UUID;
import static com.zee.graphqlcourse.search.outsourced.OutsourcedSearchQuery.OUTSOURCED_UUID;


/**
 * @author : Ezekiel Eromosei
 * @code @created : 29 Aug, 2024
 */

@Service
@RequiredArgsConstructor
public class EmployeeOutsourcedService {

    private final EmployeeRepository employeeRepository;
    private final OutsourcedRepository outsourcedRepository;
    private final AddressService addressService;
    private final MapperUtil mapperUtil;
    private final EmployeeSearchQuery employeeSearchQuery;
    private final OutsourcedSearchQuery outsourcedSearchQuery;


    public CreationResponse createEmployeeOutsourced(EmployeeOutsourcedInput input) {
        return (!StringUtils.hasText(input.getOutsourceId()) && input.getDuty() == null)
                ? createEmployee(input) : createOutsourced(input);
    }

    private CreationResponse createEmployee(EmployeeOutsourcedInput input) {
        if(!StringUtils.hasText(input.getEmployeeId()) || !StringUtils.hasText(input.getDepartmentNo())
            || !StringUtils.hasText(input.getEmail()) || input.getRole() == null) {
            throw new ProcessException("employeeId, departmentNo, email, role is required");
        }

        EmployeeDto employeeDto = mapperUtil.mapToEmployeeDto(input);
        Employee persisedEmployee = employeeRepository.save(mapperUtil.mapToEmployeeEntity(employeeDto));

        List<Address> addressList = input.getAddress().stream()
                .map(mapperUtil::mapToAddressDto)
                .map(dto -> {
                    Address address = mapperUtil.mapToAddressEntity(dto);
                    address.setEntityId(persisedEmployee.getEmployeeId());
                    return address;
                })
                .toList();

        addressService.saveAll(addressList);

        return CreationResponse.newBuilder()
                .uuid(persisedEmployee.getUuid().toString())
                .message("Employee with name " + persisedEmployee.getName() + " created successfully")
                .success(true)
                .build();
    }

    private CreationResponse createOutsourced(EmployeeOutsourcedInput input) {
        OutsourcedDto outsourcedDto = mapperUtil.mapToOutsourcedDto(input);
        Outsourced persistedOutsourced = outsourcedRepository.save(mapperUtil.mapToOutsourcedEntity(outsourcedDto));

        List<Address> addressList = input.getAddress().stream()
                .map(mapperUtil::mapToAddressDto)
                .map(dto -> {
                    Address address = mapperUtil.mapToAddressEntity(dto);
                    address.setEntityId(persistedOutsourced.getOutsourceId());
                    return address;
                })
                .toList();

        addressService.saveAll(addressList);

        return CreationResponse.newBuilder()
                .uuid(persistedOutsourced.getUuid().toString())
                .message("Outsourced with name " + persistedOutsourced.getName() + " created successfully")
                .success(true)
                .build();
    }


    public CreationResponse updateExistingEmployeeAddress(AddressInput input) {

        Optional<AddressDto> optionalAddressDto = addressService.findAddressByEntityIdAndUuid(input.getEntityId(), UUID.fromString(input.getUuid()));


        AddressDto foundAddress = optionalAddressDto.orElseThrow(() -> new NotFoundException("address for employee with id: '" + input.getEntityId() + "'not found"));

        //todo assume no input is null for now: we will do validation later
        // change to specification later

        if(input.getEntityId().trim().equalsIgnoreCase(foundAddress.getEntityId())
                && input.getUuid().trim().equalsIgnoreCase(foundAddress.getUuid())
                && input.getStreet().trim().equalsIgnoreCase(foundAddress.getStreet())
                && input.getCity().trim().equalsIgnoreCase(foundAddress.getCity())
                && input.getState().trim().equalsIgnoreCase(foundAddress.getState())
                && input.getZipCode() == foundAddress.getZipCode()){
            throw new ProcessException("address for employee with id: '" + input.getEntityId() +"'already exist");
        }


        AddressDto addressDto = mapperUtil.mapToAddressDto(input);

        Address newAddress = mapperUtil.mapToAddressEntity(addressDto);
        newAddress.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        Address persistedAddress = addressService.save(newAddress);

        return CreationResponse.newBuilder()
                .uuid(persistedAddress.getUuid().toString())
                .message("Employee Address updated successfully")
                .success(true)
                .build();
    }

    public CreationResponse updateEmployeeDetails(EmployeeUpdateInput input) {

        Employee employee = employeeRepository.findByEmployeeId(input.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("employee with id: '" + input.getEmployeeId() + "'not found"));


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

    public Person employeeSearchByStaffId(String id) {

        Optional<Employee> optionalEmployee = employeeRepository.findByEmployeeId(id);

        Outsourced outsourced = null;
        if(optionalEmployee.isEmpty()){
            outsourced = outsourcedRepository.findByOutsourceId(id)
                    .orElseThrow(() -> new NotFoundException("employee with id: '" + id + "'not found"));
        }


        if(optionalEmployee.isPresent()){
            EmployeeDto employeeDto = mapperUtil.mapToEmployeeDto(optionalEmployee.get());
            employeeDto.setAddress(addressService.findAddressByEntityId(employeeDto.getEmployeeId()));
            return employeeDto;
        }else {
            OutsourcedDto outsourcedDto = mapperUtil.mapToOutsourcedDto(outsourced);
            outsourcedDto.setAddress(addressService.findAddressByEntityId(outsourcedDto.getOutsourceId()));
            return outsourcedDto;
        }
    }


    public List<Person> employeeSearch(Boolean outsourced) {
        List<Person> personList = new ArrayList<>();
        if(outsourced){
            List<OutsourcedDto> outsourcedDtoList = outsourcedRepository.findAll()
                    .stream()
                    .map(mapperUtil::mapToOutsourcedDto)
                    .peek(outsourcedDto -> {
                        List<AddressDto> address = addressService.findAddressByEntityId(outsourcedDto.getOutsourceId());
                        outsourcedDto.setAddress(address);
                    })
                    .toList();
            personList.addAll(outsourcedDtoList);
        }else {
            List<EmployeeDto> employeeDtoList = employeeRepository.findAll()
                    .stream()
                    .map(mapperUtil::mapToEmployeeDto)
                    .peek(employeeDto -> {
                        List<AddressDto> address = addressService.findAddressByEntityId(employeeDto.getEmployeeId());
                        employeeDto.setAddress(address);
                    })
                    .toList();
            personList.addAll(employeeDtoList);
        }
        return personList;
    }

    public EmployeePagination employeeSearch(Optional<EmployeeSearchInput> inputSearch, Integer page, Integer size,
                                             DataFetchingEnvironment dataFetchingEnvironment) {
        if(page == null || page < 1) {
            page = 0;
        }

        if(size == null || size < 1) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page -1, size, Sort.Direction.ASC, EMPLOYEE_UUID);

        Page<Employee> pagedEmployees = inputSearch
                .map(employeeSearchInput ->
                        employeeRepository
                                .findAll(employeeSearchQuery.buildEmployeeSearchParams(employeeSearchInput), pageable)
                ).orElseGet(() -> employeeRepository.findAll(pageable));

        Connection<Employee> connection = new SimpleListConnection<>(pagedEmployees.getContent()).get(dataFetchingEnvironment);

        EmployeePagination employeePagination = new EmployeePagination();
        employeePagination.setEmployeeConnection(connection);
        employeePagination.setPage(page);
        employeePagination.setSize(size);
        employeePagination.setTotalElement(pagedEmployees.getTotalElements());
        employeePagination.setTotalPage(pagedEmployees.getTotalPages());

        return employeePagination;
    }

    public Connection<Outsourced> outsourcedSearch(Optional<OutsourcedSearchInput> input, DataFetchingEnvironment dataFetchingEnvironment) {

        if(input.isPresent()){
            List<Sort.Order> orders = SpecUtil.buildSortOrders(input.get().getSorts());

            List<Outsourced> results = outsourcedRepository.findAll(
                    outsourcedSearchQuery.buildOutsourcedSearchParams(input.get()),
                    Sort.by(orders)
            );

            return new SimpleListConnection<>(results).get(dataFetchingEnvironment);

        }else {
            List<Outsourced> results = outsourcedRepository.findAll(PageRequest.of(0, 10, Sort.Direction.ASC, OUTSOURCED_UUID))
                    .stream().toList();
            return new SimpleListConnection<>(results).get(dataFetchingEnvironment);
        }
    }
}
