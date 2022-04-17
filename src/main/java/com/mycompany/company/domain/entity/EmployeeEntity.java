package com.mycompany.company.domain.entity;

import com.mycompany.company.constant.Position;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "employee")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity extends AbstractBaseEntity {

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "family_name")
    private String familyName;
    @Column
    private String pin;
    @Column
    private int age;
    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToOne(targetEntity = DepartmentEntity.class)
    @JoinColumn(name = "departament_id", referencedColumnName = "id")
    private DepartmentEntity department;

    @OneToOne(targetEntity = DirectorateEntity.class)
    private DirectorateEntity directorate;

    @Column
    private String username;
    @Column
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employees_roles",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
