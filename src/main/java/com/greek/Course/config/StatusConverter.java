package com.greek.Course.config;

import com.greek.Course.model.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

/**
 * JPA 2.1或更改版本使用的枚举转换方式
 * 使用 @Enumerated(EnumType.STRING) 指定枚举转换方式在 Junit5 集成测试中不生效
 *
 * @author Zhaofeng Zhou
 * @date 2022/6/14
 */
@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {
    @Override
    public String convertToDatabaseColumn(Status attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(Status.values())
                .filter(e -> e.name().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
