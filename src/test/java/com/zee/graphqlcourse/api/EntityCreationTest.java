package com.zee.graphqlcourse.api;

import com.zee.graphqlcourse.codegen.types.*;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
@AutoConfigureGraphQlTester
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntityCreationTest {

//    @Autowired
//    HttpGraphQlTester httpGraphQlTester;

    @Autowired
    GraphQlTester httpGraphQlTester;

    @Autowired
    Faker faker;

    static String companyName;
    static String departmentNo;
    static String rcNumber;
    static String headOffice;
    static String country;

    static String street;
    static String city;
    static String state;
    static String zipcode;
    static String employeeId;


    @BeforeAll
    static void beforeAll() {
        Faker faker = new Faker();
        companyName = faker.company().name();
        rcNumber ="RC" + faker.number().randomNumber(4, true);
        headOffice = faker.address().fullAddress();
        country = faker.country().name();
        departmentNo= String.valueOf(faker.number().randomNumber(6, true));

        employeeId = faker.idNumber().valid();


        street = faker.address().streetName();
        city = faker.address().city();
        state = faker.address().state();
        zipcode = faker.address().zipCode();

    }

    @Order(1)
    @DisplayName("create company test")
    @Test
    void createCompany() {

        //todo note: you could also use object mapper to convert object to map
        Map<String, Object> variables =
                Map.of("name", companyName,
                        "rcNumber", rcNumber,
                        "headOffice", headOffice,
                        "country", country,
                        "businessType", BusinessType.values()[faker.random().nextInt(BusinessType.values().length)]
                );

        GraphQlTester.Entity<CreationResponse, ?> entity = this.httpGraphQlTester
                .documentName("createCompanyTest")
                .variable("newCompany", variables)
                .execute()
                .path("createCompany")
                .entity(CreationResponse.class);

        log.info(entity.get().toString());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.get().getSuccess()).isTrue();
        Assertions.assertThat(entity.get().getMessage()).contains("created successfully");
    }


    @Order(2)
    @DisplayName("create department test")
    @Test
    void createDepartmentTest() {

        Map<String, Object> address = Map.of(
                "entityId", departmentNo,
                "street", street,
                "city", city,
                "state", state,
                "zipCode", Integer.parseInt(zipcode)
        );

        Map<String, Object> variables =
                Map.of("name", companyName,
                        "departmentNo", departmentNo,
                        "companyName", companyName,
                        "rcNumber", rcNumber,
                        "division", Division.values()[faker.random().nextInt(Division.values().length)],
                        "address", address
                );

        GraphQlTester.Entity<CreationResponse, ?> entity = this.httpGraphQlTester
                .documentName("createDepartmentTest")
                .variable("newDepartment", variables)
                .execute()
                .path("createDepartment")
                .entity(CreationResponse.class);

        log.info(entity.get().toString());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.get().getSuccess()).isTrue();
        Assertions.assertThat(entity.get().getMessage()).contains("created successfully");
    }

    @Order(3)
    @DisplayName("create employee test")
    @Test
    void createEmployeeTest() {

        Map<String, Object> address = Map.of(
                "entityId", departmentNo,
                "street", street,
                "city", city,
                "state", state,
                "zipCode", Integer.parseInt(zipcode)
        );

        LocalDate dob= faker.date().birthdayLocalDate(20, 30);

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", faker.name().fullName());
        variables.put("dateOfBirth", dob.toString());
        variables.put("gender", Gender.values()[faker.random().nextInt(Gender.values().length)]);
        variables.put("salary", faker.number().randomDouble(2, 50000, 80000));
        variables.put("age", LocalDate.now().getYear() - dob.getYear());
        variables.put("phone", faker.phoneNumber().phoneNumber());
        variables.put("companyName", companyName);


        variables.put("employeeId", employeeId);
        variables.put("departmentNo", departmentNo);
        variables.put("email", faker.internet().emailAddress());
        variables.put("role", Role.values()[faker.random().nextInt(Role.values().length)]);
        variables.put("address", address);


        GraphQlTester.Entity<CreationResponse, ?> entity = this.httpGraphQlTester
                .documentName("createEmployeeTest")
                .variable("newEmployee", variables)
                .execute()
                .path("createEmployee")
                .entity(CreationResponse.class);

        log.info(entity.get().toString());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.get().getSuccess()).isTrue();
        Assertions.assertThat(entity.get().getMessage()).contains("created successfully");
    }


    @Order(4)
    @DisplayName("fetch All company")
    @Test
    void fetchAllCompany() {
        GraphQlTester.Entity<AllCompanyResponse, ?> entity = this.httpGraphQlTester
                .documentName("fetchAllCompanyTest")
                .execute()
                .path("fetchAllCompany")
                .entity(AllCompanyResponse.class);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.get().getCompanies()).hasSizeGreaterThan(3);
        Assertions.assertThat(
                entity.get().getCompanies().stream()
                        .map(CompanyDto::getName)
                        .toList()
                ).contains(companyName);
    }

    @Order(5)
    @DisplayName("fetch Employee and Company")
    @Test
    void employeeWithCompanySearchTest() {
        GraphQlTester.EntityList<PersonAndEntitySearch> personAndEntitySearchEntityList = this.httpGraphQlTester
                .documentName("employeeWithCompanySearchTest")
                .variable("id", employeeId)
                .execute()
                .path("employeeWithCompanySearch")
                .entityList(PersonAndEntitySearch.class);

        Assertions.assertThat(personAndEntitySearchEntityList).isNotNull();
        Assertions.assertThat(personAndEntitySearchEntityList.get().getFirst()).isInstanceOfAny(CompanyDto.class, EmployeeDto.class);

        EmployeeDto employeeDto = (EmployeeDto) personAndEntitySearchEntityList.get().getFirst();

        log.info("EmployeeDto : {}", employeeDto.toString());
        Assertions.assertThat(employeeDto.getEmployeeId()).isEqualTo(employeeId);
        Assertions.assertThat(employeeDto.getCompanyName()).isEqualTo(companyName);
        Assertions.assertThat(employeeDto.getDepartmentNo()).isEqualTo(departmentNo);
    }


}