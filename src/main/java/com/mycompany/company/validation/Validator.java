package com.mycompany.company.validation;

import com.mycompany.company.constant.ErrorType;
import com.mycompany.company.domain.entity.AbstractBaseEntity;
import com.mycompany.company.domain.model.dto.AbstractBaseDto;
import com.mycompany.company.exception.BusinessException;
import com.mycompany.company.exception.ErrorModel;
import com.mycompany.company.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Validator {

    /**
     * @description: If object is not exist, throw NotFoundException with code and message
     */
    public void validateObject(AbstractBaseEntity entityById, String errorMessage) {
        log.info("Entered in check request validator ");
        if (entityById == null) {
            List<ErrorModel> errorModelList = new ArrayList<>();

            ErrorModel errorModel = new ErrorModel();
            errorModel.setCode(ErrorType.NOT_FOUND.toString());
            errorModel.setMessage(errorMessage);
            errorModel.setTime(LocalDateTime.now());

            errorModelList.add(errorModel);

            log.warn("Object is not found, throw NotFoundException.");
            throw new NotFoundException(errorModelList);
        }
    }

    /**
     * @description: If object is exist, but required params are null ot empty, throw BusinessException with code and message
     */
    public void validateInputData(AbstractBaseDto model, String... fields) throws BusinessException {
        if (model != null) {
            List<ErrorModel> errorModelList = new ArrayList<>();
            for (String field : fields) {
                if (field == null || field.isEmpty()) {
                    ErrorModel errorModel = new ErrorModel();

                    errorModel.setCode(ErrorType.BAD_REQUEST.toString());
                    errorModel.setMessage("Cannot create model without required params.");
                    errorModel.setTime(LocalDateTime.now());
                    errorModelList.add(errorModel);

                    throw new BusinessException(errorModelList);
                }
            }
        }
    }

    /**
     * @description: Validate object for duplicating
     */
    public void validateObjectForDuplicating(Object... objects) throws BusinessException {
        for (Object optional : objects) {
            if (optional != null) {
                List<ErrorModel> errorModelList = new ArrayList<>();

                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode(ErrorType.ALREADY_EXIST.toString());
                errorModel.setMessage("This account already exist.");
                errorModel.setTime(LocalDateTime.now());
                errorModelList.add(errorModel);

                throw new BusinessException(errorModelList);
            }
        }
    }
}
