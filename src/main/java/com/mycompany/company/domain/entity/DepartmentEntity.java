package com.mycompany.company.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "department")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepartmentEntity extends AbstractBaseEntity {

    @Column
    private String name;
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "directorate_id", referencedColumnName = "id")
    private DirectorateEntity directorate;

    //todo stay unidirectional at the moment
//    @OneToMany(targetEntity = EmployeeEntity.class)
//    @JoinColumn(name = "employee_id", referencedColumnName = "id")
//    private Set<EmployeeEntity> employees;

}
