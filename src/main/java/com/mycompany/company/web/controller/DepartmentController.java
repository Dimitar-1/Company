package com.mycompany.company.web.controller;

import com.mycompany.company.domain.model.dto.DepartmentDto;
import com.mycompany.company.exception.BusinessException;
import com.mycompany.company.exception.NotFoundException;
import com.mycompany.company.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: This controller is accountable for providing crud operations.
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public DepartmentController(DepartmentService departmentService, ModelMapper modelMapper) {
        this.departmentService = departmentService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentDto> all = departmentService.findAll();

        log.info("Return all departments");
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/departments/{departmentId}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable(name = "departmentId") long id) throws NotFoundException {
        DepartmentDto byId = departmentService.findById(id).get();

        log.info("Return employee with id: " + id);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/departments")
    public ResponseEntity<Long> createDepartment(@RequestBody DepartmentDto departmentModel) throws BusinessException {
        long save = departmentService.save(departmentModel);

        log.info("Create new department: " + departmentModel.toString());
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/departments/{departmentId}")
    public ResponseEntity<DepartmentDto> updateDepartmentById(@PathVariable(name = "departmentId") long id, @RequestBody DepartmentDto departmentModel) throws NotFoundException, BusinessException {
        DepartmentDto updated = departmentService.update(id, departmentModel);

        log.info("Update department with id: " + id + " New department vision: " + updated.toString());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/departments/{departmentId}")
    public ResponseEntity<String> deleteDepartmentById(@PathVariable(name = "departmentId") long id) throws NotFoundException {
        departmentService.deleteById(id);

        log.info("Deleted department was with id: " + id);
        return new ResponseEntity<>("Department was successfully deleted.", HttpStatus.NO_CONTENT);
    }
}
