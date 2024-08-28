package com.zee.graphqlcourse.entity;

import com.zee.graphqlcourse.codegen.types.Duty;
import com.zee.graphqlcourse.codegen.types.Gender;
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
@Table(name = "outsourced", indexes = {
        @Index(name = "comp_emp_id_dept_no", columnList = "company_name, outsource_id", unique = true)
})
public class Outsourced {

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
    private boolean active = true;

    private String outsourceId;
    @Column(length = 11, nullable = false)
    private Duty duty;


}
