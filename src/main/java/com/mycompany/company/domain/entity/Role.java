package com.mycompany.company.domain.entity;

import com.mycompany.company.constant.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity(name = "roles")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractBaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    @Column(name = "role_description")
    private String roleDescription;
}