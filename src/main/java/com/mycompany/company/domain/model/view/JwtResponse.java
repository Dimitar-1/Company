package com.mycompany.company.domain.model.view;

import com.mycompany.company.domain.model.dto.EmployeeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private EmployeeDto employee;
    private String jwtToken;
}
