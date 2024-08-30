package com.zee.graphqlcourse.entity;

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
@Table(name = "address", indexes = {
        @Index(name = "ent_id_idx", columnList = "entity_id"),
        @Index(name = "ent_id_street", columnList = "entity_id, street", unique = true)
})
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String entityId;
    private String street;
    private String city;
    private String state;
    private int zipCode;
}
