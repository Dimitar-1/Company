package com.mycompany.company.domain.model.view;

import lombok.Data;

@Data
public class JwtRequest {

    private String username;
    private String password;

}
