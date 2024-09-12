package com.zee.graphqlcourse.enumconverter;

import com.zee.graphqlcourse.codegen.types.Gender;
import com.zee.graphqlcourse.exception.ProcessException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 27 Aug, 2024
 */

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute.name();
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.name().equalsIgnoreCase(dbData))
                .findFirst().orElseThrow(ProcessException::new);
    }
}
