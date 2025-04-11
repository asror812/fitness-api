package com.example.demo.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonConfigTest {

    @Test
    void objectMapper_shouldRegisterJavaTimeModule_andExcludeNulls() {
        JacksonConfig config = new JacksonConfig();

        ObjectMapper objectMapper = config.objectMapper();

        assertThat(objectMapper.getSerializationConfig().getDefaultPropertyInclusion().getValueInclusion())
                .isEqualTo(JsonInclude.Include.NON_NULL);

    }
}
