package com.mycompany.company.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mycompany.company.constant.Position;
import com.mycompany.company.domain.entity.Role;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto extends AbstractBaseDto {

    private String firstName;
    private String familyName;
    private String pin;
    private int age;
    private Position position;

    private DepartmentDto department;

    private DirectorateDto directorate;

    private String username;
    private String password;
    private Set<Role> roles = new HashSet<>();
}
