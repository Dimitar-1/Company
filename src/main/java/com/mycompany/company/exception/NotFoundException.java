package com.mycompany.company.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private List<ErrorModel> errorModelList;

    public NotFoundException(List<ErrorModel> errorModelList) {
        this.errorModelList = errorModelList;
    }
}