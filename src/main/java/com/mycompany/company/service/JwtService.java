package com.mycompany.company.service;

import com.mycompany.company.domain.model.view.JwtRequest;
import com.mycompany.company.domain.model.view.JwtResponse;

public interface JwtService {

    JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception;
}
