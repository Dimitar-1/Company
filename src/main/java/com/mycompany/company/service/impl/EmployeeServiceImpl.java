package com.mycompany.company.service.impl;

import com.mycompany.company.constant.ErrorType;
import com.mycompany.company.constant.RoleEnum;
import com.mycompany.company.domain.entity.DepartmentEntity;
import com.mycompany.company.domain.entity.EmployeeEntity;
import com.mycompany.company.domain.entity.Role;
import com.mycompany.company.domain.model.dto.EmployeeDto;
import com.mycompany.company.domain.model.view.BaseEmployeeView;
import com.mycompany.company.exception.BusinessException;
import com.mycompany.company.exception.ErrorModel;
import com.mycompany.company.exception.NotFoundException;
import com.mycompany.company.repository.EmployeeRepository;
import com.mycompany.company.repository.RoleRepository;
import com.mycompany.company.service.EmployeeService;
import com.mycompany.company.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: This class is responsive for business logic - crud for employee
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, Validator validator, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * @args: void
     * @return: List<EmployeeModel>
     * @description: This method return all employees
     */
    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDto.class))
                .collect(Collectors.toList());
    }

    /**
     * @args: int id
     * @return: Optional<EmployeeModel>
     * @description: This method return employee by id
     */
    @Override
    public Optional<EmployeeDto> findById(Long id) throws NotFoundException {
        log.debug("Entering method findById in service layer");
        EmployeeEntity byId = employeeRepository.findById(id).orElse(null);
        validator.validateObject(byId, "Employee with id was not found");

        log.info("Account is successfully found by id");
        return Optional.of(modelMapper.map(byId, EmployeeDto.class));
    }

    /**
     * @args: EmployeeModel model
     * @return: new employee id - long
     * @description: This method create and return employee by id or throw business exception if is already exist
     */
    @Override
    @Transactional
    public long save(EmployeeDto model) throws BusinessException {
        log.debug("Entering method save in service layer");
        validator.validateInputData(model);

        EmployeeEntity employeeEntity = employeeRepository.findById(model.getId()).orElse(null);
        validator.validateObjectForDuplicating(employeeEntity);

        if (model.getPassword() == null || model.getPassword().isEmpty()
                || model.getUsername() == null || model.getUsername().isEmpty()
                || model.getFirstName() == null || model.getFirstName().isEmpty()
                || model.getPin() == null || model.getPin().isEmpty()) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            ErrorModel errorModel = ErrorModel.builder()
                    .code(ErrorType.BAD_REQUEST.toString())
                    .message("Username and password cannot be empty.")
                    .time(LocalDateTime.now())
                    .build();

            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        model.setPassword(passwordEncoder.encode(model.getPassword()));
        if (model.getRoles().isEmpty()) {
            Set<Role> roleSet = new HashSet<>();
            Role role = new Role();
            role.setRoleName(RoleEnum.USER);
            role.setRoleDescription("user default role");
            roleRepository.save(role);

            roleSet.add(role);
            model.setRoles(roleSet);
        }

        EmployeeEntity map = modelMapper.map(model, EmployeeEntity.class);
        EmployeeEntity saved = employeeRepository.save(map);

        log.info("Employee is successfully created");
        return saved.getId();
    }

    /**
     * @args: Long id, EmployeeModel model
     * @return: EmployeeModel update
     * @description: This method update employee by model as input
     */
    @Override
    public EmployeeDto update(Long id, EmployeeDto model) throws NotFoundException {
        log.debug("Entering method update in service layer");

        EmployeeEntity oldEntity = employeeRepository.findById(id).orElse(null);
        validator.validateObject(oldEntity, "Employee with this id is not found. Cannot be updated.");

        deleteById(oldEntity.getId());

        EmployeeEntity map = modelMapper.map(model, EmployeeEntity.class);

        EmployeeEntity saved = employeeRepository.save(map);
        log.info("Employee is successfully updated");
        return modelMapper.map(saved, EmployeeDto.class);
    }

    /**
     * @args: Long id
     * @return: void
     * @description: This method delete employee if exist by id
     */
    @Override
    public void deleteById(Long id) throws NotFoundException {
        EmployeeEntity byId = employeeRepository.findById(id).orElse(null);
        validator.validateObject(byId, "Employee with this id is not found. Cannot be deleted.");

        byId.setDepartment(null);
        byId.setDirectorate(null);

        employeeRepository.delete(byId);

        log.info("Employee was successfully deleted");
    }

}