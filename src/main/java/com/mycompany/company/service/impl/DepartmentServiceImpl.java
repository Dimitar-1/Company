package com.mycompany.company.service.impl;

import com.mycompany.company.domain.entity.DepartmentEntity;
import com.mycompany.company.domain.model.dto.DepartmentDto;
import com.mycompany.company.exception.BusinessException;
import com.mycompany.company.exception.NotFoundException;
import com.mycompany.company.repository.DepartmentRepository;
import com.mycompany.company.service.DepartmentService;
import com.mycompany.company.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description: This class is responsive for business logic - crud for department
 */
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, Validator validator, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    /**
     * @args: void
     * @return: List<DepartmentModel>
     * @description: This method return all departments
     */
    @Override
    public List<DepartmentDto> findAll() {
        return departmentRepository.findAll().stream()
                .map(departmentEntity -> modelMapper.map(departmentEntity, DepartmentDto.class))
                .collect(Collectors.toList());
    }

    /**
     * @args: int id
     * @return: Optional<DepartmentModel>
     * @description: This method return department by id
     */
    @Override
    public Optional<DepartmentDto> findById(Long id) throws NotFoundException {
        log.debug("Entering method findById in service layer");
        DepartmentEntity byId = departmentRepository.findById(id).orElse(null);
        validator.validateObject(byId, "Department with id was not found");

        log.info("Department was successfully found by id");
        return Optional.of(modelMapper.map(byId, DepartmentDto.class));
    }

    /**
     * @args: DepartmentModel model
     * @return: new department id - long
     * @description: This method create and return department by id or throw business exception if is already exist
     */
    @Override
    @Transactional
    public long save(DepartmentDto model) throws BusinessException {
        log.debug("Entering method save in service layer");
        validator.validateInputData(model, model.getDescription(), model.getName());

        DepartmentEntity departmentEntity = departmentRepository.findById(model.getId()).orElse(null);
        validator.validateObjectForDuplicating(departmentEntity);

        DepartmentEntity map = modelMapper.map(model, DepartmentEntity.class);
        DepartmentEntity saved = departmentRepository.save(map);

        log.info("Department is successfully created");
        return saved.getId();
    }

    /**
     * @args: Long id, DepartmentModel model
     * @return: DepartmentModel update
     * @description: This method update department by model as input
     */
    @Override
    public DepartmentDto update(Long id, DepartmentDto model) throws NotFoundException {
        log.debug("Entering method update in service layer");

        DepartmentEntity oldEntity = departmentRepository.findById(id).orElse(null);
        validator.validateObject(oldEntity, "Department with this id is not found. Cannot be updated.");

        deleteById(oldEntity.getId());

        DepartmentEntity map = modelMapper.map(model, DepartmentEntity.class);

        DepartmentEntity saved = departmentRepository.save(map);
        log.info("Department is successfully updated");
        return modelMapper.map(saved, DepartmentDto.class);
    }

    /**
     * @args: Long id
     * @return: void
     * @description: This method delete department if exist by id
     */
    @Override
    public void deleteById(Long id) throws NotFoundException {
        DepartmentEntity byId = departmentRepository.findById(id).orElse(null);
        validator.validateObject(byId, "Department with this id is not found. Cannot be deleted.");

        byId.setDirectorate(null);
//        byId.setEmployees(null);

        departmentRepository.delete(byId);

        log.info("Department is successfully deleted");
    }
}