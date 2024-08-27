package com.zee.graphqlcourse.entity;

import com.zee.graphqlcourse.codegen.types.Gender;
import com.zee.graphqlcourse.codegen.types.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 27 Aug, 2024
 */

@Getter
@Setter
@Entity
@Table(name = "employee", indexes = {
        @Index(name = "comp_emp_id_dept_no", columnList = "company_name, employee_id, department_no", unique = true)
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String name;
    private LocalDate dateOfBirth;
    @Column(length = 6, nullable = false)
    private Gender gender;
    @Column(nullable = false)
    private BigDecimal salary;
    private int age;
    private String phone;
    private String companyName;

    private String employeeId;
    private String departmentNo;
    private String email;
    @Column(length = 10, nullable = false)
    private Role role;


}
