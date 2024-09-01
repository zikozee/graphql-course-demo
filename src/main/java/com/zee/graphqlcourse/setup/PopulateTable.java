package com.zee.graphqlcourse.setup;

import com.zee.graphqlcourse.codegen.types.*;
import com.zee.graphqlcourse.entity.*;
import com.zee.graphqlcourse.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 31 Aug, 2024
 */

@Slf4j
@ConditionalOnProperty(name = "populate.table.enabled", havingValue = "true")
@Component
@RequiredArgsConstructor
public class PopulateTable implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final OutsourcedRepository outsourcedRepository;
    private final AddressRepository addressRepository;
    private final Faker faker;



    @Override
    public void run(String... args) throws Exception {

        // create 3 companies with the three business type

        // create the four arm of department linked to company based on Division

        //create 200 employees with the four roles , 40 outsourced 3 duties

        // link each of the employees/outsourced to different addresses

        //TODO save one by one to catch index errors
        log.info("<<<<<<<<<<<<<<<<<<<<< Populating table Bean triggered: >>>>>>>>>>>>>>>>>>>>>>>>");

        List<Company> savedCompanies = generateCompanies();
        List<Department> savedDepartments = generateDepartments(savedCompanies);
        List<Employee> employees = generateEmployees(savedDepartments);
        List<Outsourced> outsourcedList = generateOutsourced(savedDepartments);

        generateAddress(savedDepartments, employees, outsourcedList);


    }


    private List<Company> generateCompanies(){
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Company company = new Company();
            company.setName(faker.company().name());
            company.setRcNumber("RC" + faker.number().randomNumber(4, true));
            company.setHeadOffice(faker.address().fullAddress());
            company.setCountry(faker.country().name());
            company.setBusinessType(BusinessType.values()[i]);

            companies.add(company);
        }

        return companyRepository.saveAll(companies);
    }

    private List<Department> generateDepartments(List<Company> savedCompanies){
        List<Department> allDepartments = savedCompanies.stream()
                .map(company -> {
                    List<Department> departments = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        Department department = new Department();

                        department.setName(faker.commerce().department());
                        department.setDepartmentNo(String.valueOf(faker.number().randomNumber(6, true)));
                        department.setCompanyName(company.getName());
                        department.setRcNumber(company.getRcNumber());
                        department.setDivision(Division.values()[i]);

                        departments.add(department);
                    }
                    return departments;
                }).flatMap(List::stream).toList();

        /*
         * todo: save one by one to catch errors
         */

        return allDepartments.stream()
               .map(department -> {
                   try {
                       return departmentRepository.save(department);
                   }catch (Exception _){
                       log.info("department save failed: {}", department.getName());
                       return null;
                   }
               }).filter(Objects::nonNull)
               .toList();
    }

    private List<Employee> generateEmployees(List<Department> savedDepartments){
        List<Employee> allEmployees = savedDepartments.stream()
                .map(dept -> {
                    List<Employee> employees = new ArrayList<>();
                    for (int i = 0; i < 200; i++) {

                        Employee employee = new Employee();

                        employee.setName(faker.name().fullName());
                        employee.setDateOfBirth(LocalDate.now().minusYears(22 + (faker.number().numberBetween(1, 20))));
                        employee.setGender(Gender.values()[faker.number().numberBetween(0, 2)]);
                        employee.setSalary(BigDecimal.valueOf(faker.number().randomDouble(2, 30000, 100000)));
                        employee.setAge(LocalDate.now().getYear() - employee.getDateOfBirth().getYear());
                        employee.setPhone(faker.phoneNumber().phoneNumber());
                        employee.setCompanyName(dept.getCompanyName());
                        employee.setActive(true);

                        employee.setEmployeeId(faker.idNumber().valid());
                        employee.setDepartmentNo(dept.getDepartmentNo());
                        employee.setEmail(faker.internet().emailAddress());
                        employee.setRole(Role.values()[faker.number().numberBetween(0, 3)]);

                        setupEmployees(i, employee);
                        employees.add(employee);
                    }

                    return employees;
                }).flatMap(List::stream).toList();


        return allEmployees.stream()
                .map(employee -> {
                    try {
                        return employeeRepository.save(employee);
                    }catch (Exception _){
                        log.info("employee save failed: {}", employee.getName());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();
    }


    private List<Outsourced> generateOutsourced(List<Department> savedDepartments){
        List<Outsourced> allOutsourced = savedDepartments.stream()
                .map(dept -> {
                    List<Outsourced> outsourcedList = new ArrayList<>();
                    for (int i = 0; i < 30; i++) {

                        Outsourced outsourced = new Outsourced();

                        outsourced.setName(faker.name().fullName());
                        outsourced.setDateOfBirth(LocalDate.now().minusYears(22 + (faker.number().numberBetween(1, 20))));
                        outsourced.setGender(Gender.values()[faker.number().numberBetween(0, 2)]);
                        outsourced.setSalary(BigDecimal.valueOf(faker.number().randomDouble(2, 30000, 100000)));
                        outsourced.setAge(LocalDate.now().getYear() - outsourced.getDateOfBirth().getYear());
                        outsourced.setPhone(faker.phoneNumber().phoneNumber());
                        outsourced.setCompanyName(dept.getCompanyName());
                        outsourced.setActive(true);

                        outsourced.setOutsourceId(faker.idNumber().valid());
                        outsourced.setDuty(Duty.values()[faker.number().numberBetween(0,3)]);

                        outsourcedList.add(outsourced);
                    }

                    return outsourcedList;
                }).flatMap(List::stream).toList();

        /*
         * todo: save one by one to catch errors
         */

        return allOutsourced.stream()
                .map(outsourced -> {
                    try {
                        return outsourcedRepository.save(outsourced);
                    }catch (Exception _){
                        log.info("outsourced save failed: {}", outsourced.getName());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();
    }

    private void generateAddress( List<Department> savedDepartments, List<Employee> employees, List<Outsourced> outsourcedList) {

        List<Address> allAddresses = new ArrayList<>();
        for(Department department : savedDepartments) {
            Address address = new Address();
            address.setEntityId(department.getDepartmentNo());
            commonAddressDetails(allAddresses, address);
        }

        for(Employee employee : employees) {
            Address address = new Address();
            address.setEntityId(employee.getEmployeeId());
            commonAddressDetails(allAddresses, address);
        }

        for(Outsourced outsourced: outsourcedList){
            Address address = new Address();
            address.setEntityId(outsourced.getOutsourceId());
            commonAddressDetails(allAddresses, address);
        }

        allAddresses.forEach(address -> {
                    try {
                        addressRepository.save(address);
                    }catch (Exception _){
                        log.info("address save failed: {}", address.getEntityId());
                    }
                });
    }

    private void commonAddressDetails(List<Address> allAddresses, Address address) {
        address.setStreet(faker.address().fullAddress());
        address.setCity(faker.address().city());
        address.setState(faker.address().state());
        address.setZipCode(
                isDigits(faker.address().zipCode()) ? Integer.parseInt(faker.address().zipCode()) : faker.number().numberBetween(100000, 50000)
        );

        allAddresses.add(address);
    }


    private void setupEmployees(int index, Employee employee) {
        if(index ==99) employee.setRole(Role.GMD);
    }

    private boolean isDigits(String value){
        try{
            Integer.parseInt(value);
            return true;
        }catch (Exception _){
            return false;
        }
    }
}
