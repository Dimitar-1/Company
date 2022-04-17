package com.mycompany.company.service.impl;

import com.mycompany.company.constant.ErrorType;
import com.mycompany.company.domain.model.dto.EmployeeDto;
import com.mycompany.company.domain.model.view.JwtRequest;
import com.mycompany.company.domain.model.view.JwtResponse;
import com.mycompany.company.domain.entity.EmployeeEntity;
import com.mycompany.company.exception.ErrorModel;
import com.mycompany.company.exception.NotFoundException;
import com.mycompany.company.repository.EmployeeRepository;
import com.mycompany.company.service.JwtService;
import com.mycompany.company.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JwtServiceImpl implements UserDetailsService, JwtService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String username = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        authenticate(username, password);

        final UserDetails userDetails = loadUserByUsername(username);

        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        EmployeeEntity byUsername = employeeRepository.findUserByUsername(username);
        EmployeeDto user = modelMapper.map(byUsername, EmployeeDto.class);

        return new JwtResponse(user, newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeeEntity user = employeeRepository.findUserByUsername(username);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    getAuthorities(user)
            );
        } else {
            throw new UsernameNotFoundException("Username is not valid");
        }
    }

    private Set<SimpleGrantedAuthority> getAuthorities(EmployeeEntity user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
//            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        return authorities;
    }

    private void authenticate(String username, String userPassword) throws Exception {
        List<ErrorModel> errorModelList;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userPassword));
        } catch (DisabledException e) {
            errorModelList = new ArrayList<>();

            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(ErrorType.DISABLED.toString());
            errorModel.setMessage("User is disabled");
            errorModel.setTime(LocalDateTime.now());
            errorModelList.add(errorModel);

            throw new com.mycompany.company.exception.DisabledException(errorModelList);
        } catch (BadCredentialsException e) {
            errorModelList = new ArrayList<>();

            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(ErrorType.NOT_FOUND.toString());
            errorModel.setMessage("User not found - bad credential");
            errorModel.setTime(LocalDateTime.now());
            errorModelList.add(errorModel);

            throw new NotFoundException(errorModelList);
        }
    }
}
