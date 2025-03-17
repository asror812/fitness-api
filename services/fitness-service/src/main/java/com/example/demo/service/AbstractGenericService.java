package com.example.demo.service;

import com.example.demo.dao.GenericDAO;
import com.example.demo.mapper.GenericMapper;
import jakarta.transaction.Transactional;
import lombok.Getter;

@Getter
public abstract class AbstractGenericService<ENTITY, CREATE_DTO, UPDATE_DTO, RESPONSE_DTO, UPDATE_RESPONSE_DTO>
        implements GenericService<UPDATE_DTO, UPDATE_RESPONSE_DTO> {

    protected abstract GenericDAO<ENTITY> getDao();

    protected abstract Class<ENTITY> getEntityClass();

    protected abstract GenericMapper<ENTITY, CREATE_DTO, RESPONSE_DTO, UPDATE_DTO> getMapper();

    protected abstract UPDATE_RESPONSE_DTO internalUpdate(UPDATE_DTO updateDTO);

    @Transactional
    public UPDATE_RESPONSE_DTO update(UPDATE_DTO updateDto) {
        return internalUpdate(updateDto);
    }

}
