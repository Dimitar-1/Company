package com.mycompany.company.web.controller;

import com.mycompany.company.domain.model.dto.EmployeeDto;
import com.mycompany.company.domain.model.view.JwtRequest;
import com.mycompany.company.domain.model.view.JwtResponse;
import com.mycompany.company.service.EmployeeService;
import com.mycompany.company.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class HomeController {

    private final JwtService jwtService;
    private final EmployeeService employeeService;

    @Autowired
    public HomeController(JwtService jwtService, EmployeeService employeeService) {
        this.jwtService = jwtService;
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        return new ResponseEntity<>("Welcome, \n\r" +
                "   For login - /login,\n\r" +
                "   For register - /register,\n\r" +
                "   For documentation - /swagger-ui/index.html", HttpStatus.OK);
    }

    @PostMapping("/login")
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

    @PostMapping("/register")
    public long register(@RequestBody EmployeeDto employeeDto) throws Exception {
        return employeeService.save(employeeDto);
    }
}
