package com.mycompany.company.service.impl;

import com.mycompany.company.domain.entity.DirectorateEntity;
import com.mycompany.company.domain.model.dto.DirectorateDto;
import com.mycompany.company.exception.BusinessException;
import com.mycompany.company.exception.NotFoundException;
import com.mycompany.company.repository.DirectorateRepository;
import com.mycompany.company.service.DirectorateService;
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
 * @description: This class is responsive for business logic - crud for directorate
 */
@Service
@Slf4j
public class DirectorateServiceImpl implements DirectorateService {

    private final DirectorateRepository directorateRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public DirectorateServiceImpl(DirectorateRepository directorateRepository, Validator validator, ModelMapper modelMapper) {
        this.directorateRepository = directorateRepository;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    /**
     * @args: void
     * @return: List<DirectorateModel>
     * @description: This method return all directorates
     */
    @Override
    public List<DirectorateDto> findAll() {
        return directorateRepository.findAll().stream()
                .map(directorateEntity -> modelMapper.map(directorateEntity, DirectorateDto.class))
                .collect(Collectors.toList());
    }

    /**
     * @args: int id
     * @return: Optional<DirectorateModel>
     * @description: This method return directorate by id
     */
    @Override
    public Optional<DirectorateDto> findById(Long id) throws NotFoundException {
        log.debug("Entering method findById in service layer");
        DirectorateEntity byId = directorateRepository.findById(id).orElse(null);
        validator.validateObject(byId, "Directorate with id was not found");

        log.info("Directorate was successfully found by id");
        return Optional.of(modelMapper.map(byId, DirectorateDto.class));
    }

    /**
     * @args: DirectorateModel model
     * @return: new directorate id - long
     * @description: This method create and return directorate by id or throw business exception if is already exist
     */
    @Override
    @Transactional
    public long save(DirectorateDto model) throws BusinessException {
        log.debug("Entering method save in service layer");
        validator.validateInputData(model, model.getDescription(), model.getName());

        DirectorateEntity directorateEntity = directorateRepository.findById(model.getId()).orElse(null);
        validator.validateObjectForDuplicating(directorateEntity);

        DirectorateEntity map = modelMapper.map(model, DirectorateEntity.class);
        DirectorateEntity saved = directorateRepository.save(map);

        log.info("Directorate is successfully created");
        return saved.getId();
    }

    /**
     * @args: Long id, DirectorateModel model
     * @return: DirectorateModel update
     * @description: This method update directorate by model as input
     */
    @Override
    public DirectorateDto update(Long id, DirectorateDto model) throws NotFoundException, BusinessException {
        log.debug("Entering method update in service layer");

        DirectorateEntity oldEntity = directorateRepository.findById(id).orElse(null);
        validator.validateObject(oldEntity, "Directorate with this id is not found. Cannot be updated.");

        deleteById(oldEntity.getId());

        DirectorateEntity map = modelMapper.map(model, DirectorateEntity.class);

        DirectorateEntity saved = directorateRepository.save(map);
        log.info("Directorate is successfully updated");
        return modelMapper.map(saved, DirectorateDto.class);
    }

    /**
     * @args: Long id
     * @return: void
     * @description: This method delete directorate if exist by id
     */
    @Override
    public void deleteById(Long id) throws NotFoundException {
        DirectorateEntity byId = directorateRepository.findById(id).orElse(null);
        validator.validateObject(byId, "Directorate with this id is not found. Cannot be deleted.");

        byId.setDirector(null);
        byId.setDepartments(null);

        directorateRepository.delete(byId);

        log.info("Directorate is successfully deleted");
    }
}
