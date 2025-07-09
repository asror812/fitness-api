package com.example.demo.utils;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class ModelMapperConfigTest {

    @Test
    void modelMapper_ShouldHaveStrictMatchingStrategy() {
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();

        assertNotNull(modelMapper);
        assertEquals(org.modelmapper.convention.MatchingStrategies.STRICT,
                modelMapper.getConfiguration().getMatchingStrategy());
    }
}
