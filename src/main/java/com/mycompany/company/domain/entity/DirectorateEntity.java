package com.mycompany.company.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "directorate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DirectorateEntity extends AbstractBaseEntity {

    @Column
    private String name;
    @Column
    private String description;

    @OneToOne(targetEntity = EmployeeEntity.class)
    private EmployeeEntity director;

    @OneToMany(mappedBy = "directorate", cascade = CascadeType.ALL)
    private List<DepartmentEntity> departments;
}
