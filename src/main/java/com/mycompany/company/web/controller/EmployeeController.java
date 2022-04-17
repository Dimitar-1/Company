package com.mycompany.company.web.controller;

import com.mycompany.company.domain.model.dto.EmployeeDto;
import com.mycompany.company.domain.model.view.BaseEmployeeView;
import com.mycompany.company.exception.BusinessException;
import com.mycompany.company.exception.NotFoundException;
import com.mycompany.company.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: This controller is accountable for providing crud operations.
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/employees")
    public ResponseEntity<List<?>> getAllEmployees(HttpServletRequest request) {
        List<?> all = employeeService.findAll().stream()
                .map(employeeDto -> modelMapper.map(employeeDto, BaseEmployeeView.class))
                .collect(Collectors.toList());
        if (request.isUserInRole("ROLE_ADMIN")) {
            all = employeeService.findAll();
        }

        log.info("Return all employees");
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable(name = "employeeId") long id, HttpServletRequest request) throws NotFoundException {
        Object byId = employeeService.findById(id).get();

        if (request.isUserInRole("ROLE_USER")) {
            byId = modelMapper.map(byId, BaseEmployeeView.class);
        }

        log.info("Return employee with id: " + id);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/employees")
    public ResponseEntity<Long> createEmployee(@RequestBody EmployeeDto employeeModel) throws BusinessException {
        long save = employeeService.save(employeeModel);

        log.info("Create new employee: " + employeeModel.toString());
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployeeById(@PathVariable(name = "employeeId") long id, @RequestBody EmployeeDto employeeModel) throws NotFoundException, BusinessException {
        EmployeeDto updated = employeeService.update(id, employeeModel);

        log.info("Update employee with id: " + id + " New employee vision: " + updated.toString());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<?> deleteAccountBy(@PathVariable(name = "employeeId") long id) throws NotFoundException {
        employeeService.deleteById(id);

        log.info("Deleted employee was with id: " + id);
        return new ResponseEntity<>("Employee was successfully deleted.", HttpStatus.NO_CONTENT);
    }

}
