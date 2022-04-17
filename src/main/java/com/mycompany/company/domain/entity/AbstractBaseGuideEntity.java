package com.mycompany.company.domain.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBaseGuideEntity extends AbstractBaseEntity {

    @Column
    private String name;
    @Column
    private String description;
}
