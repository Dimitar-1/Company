package com.mycompany.company.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DisabledException extends Exception {

    private List<ErrorModel> errorModelList;

    public DisabledException(List<ErrorModel> errorModelList) {
        this.errorModelList = errorModelList;
    }
}
