package com.mycompany.company.web.controller;

import com.mycompany.company.domain.model.dto.DirectorateDto;
import com.mycompany.company.exception.BusinessException;
import com.mycompany.company.exception.NotFoundException;
import com.mycompany.company.service.DirectorateService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: This controller is accountable for providing crud operations.
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class DirectorateController {

    private final DirectorateService directorateService;
    private final ModelMapper modelMapper;

    @Autowired
    public DirectorateController(DirectorateService directorateService, ModelMapper modelMapper) {
        this.directorateService = directorateService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/directorates")
    public ResponseEntity<List<DirectorateDto>> getAllDirectorate() {
        List<DirectorateDto> all = directorateService.findAll();

        log.info("Return all directorates");
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/directorates/{directorateId}")
    public ResponseEntity<DirectorateDto> getDirectorateById(@PathVariable(name = "directorateId") long id, HttpServletRequest request) throws NotFoundException {
        DirectorateDto byId = directorateService.findById(id).get();

        log.info("Return directorate with id: " + id);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/directorates")
    public ResponseEntity<Long> createDepartment(@RequestBody DirectorateDto directorateModel) throws BusinessException {
        long save = directorateService.save(directorateModel);

        log.info("Create new directorate: " + directorateModel.toString());
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/directorates/{directorateId}")
    public ResponseEntity<DirectorateDto> updateDirectorateById(@PathVariable(name = "directorateId") long id, @RequestBody DirectorateDto directorateModel) throws NotFoundException, BusinessException {
        DirectorateDto updated = directorateService.update(id, directorateModel);

        log.info("Update directorate with id: " + id + " New directorate vision: " + updated.toString());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/directorates/{directorateId}")
    public ResponseEntity<String> deleteDirectorateById(@PathVariable(name = "directorateId") long id) throws NotFoundException {
        directorateService.deleteById(id);

        log.info("Deleted directorate was with id: " + id);
        return new ResponseEntity<>("Directorate was successfully deleted.", HttpStatus.NO_CONTENT);
    }
}
