package com.example.demo.util;

import com.example.demo.model.Role;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.stereotype.Component;

@Component
public class MyCustomEnumConverter implements Converter<String, Role> {
    @Override
    public Role convert(String source) {
        try {
            return Role.valueOf(source);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}