package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface GenericService<UPDATE_REQUEST_DTO, UPDATE_RESPONSE_DTO> {

    UPDATE_RESPONSE_DTO update(UPDATE_REQUEST_DTO updateDTO);
}