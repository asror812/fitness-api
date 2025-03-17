package com.example.demo.mapper;

public interface GenericMapper<ENTITY, CREATE_DTO, RESPONSE_DTO, UPDATE_DTO> {

    public abstract ENTITY toEntity(CREATE_DTO createDto);

    public abstract RESPONSE_DTO toResponseDTO(ENTITY entity);

    public abstract void toEntity(UPDATE_DTO updateDto, ENTITY entity);

}