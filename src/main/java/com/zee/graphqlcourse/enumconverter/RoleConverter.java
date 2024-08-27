package com.zee.graphqlcourse.enumconverter;

import com.zee.graphqlcourse.codegen.types.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 27 Aug, 2024
 */

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute.name();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equalsIgnoreCase(dbData))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
