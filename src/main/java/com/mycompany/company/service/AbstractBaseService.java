package com.mycompany.company.service;

import com.mycompany.company.exception.BusinessException;
import com.mycompany.company.exception.NotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AbstractBaseService<T, ID extends Serializable> {

    List<T> findAll();

    Optional<T> findById(ID id) throws NotFoundException;

    long save(T model) throws BusinessException;

    T update(ID id, T model) throws NotFoundException, BusinessException;

    void deleteById(ID id) throws NotFoundException;

}