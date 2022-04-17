package com.mycompany.company.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractBaseGuideDto extends AbstractBaseDto {

    private String name;
    private String description;
}
