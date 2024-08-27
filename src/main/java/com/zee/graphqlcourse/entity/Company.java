package com.zee.graphqlcourse.entity;

import com.zee.graphqlcourse.codegen.types.BusinessType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 27 Aug, 2024
 */

@Getter
@Setter
@Entity
@Table(name = "company", indexes = {
        @Index(name = "name_idx", columnList = "name",unique = true),
        @Index(name = "rc_number_idx", columnList = "rc_number",unique = true)
})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Column(nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String rcNumber;
    @Column(nullable = false)
    private String headOffice;
    @Column(length = 56, nullable = false)
    private String country;
    private BusinessType businessType;

}
