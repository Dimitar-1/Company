package com.mycompany.company.bootstrap;

import com.mycompany.company.constant.Position;
import com.mycompany.company.constant.RoleEnum;
import com.mycompany.company.domain.entity.Role;
import com.mycompany.company.domain.entity.DepartmentEntity;
import com.mycompany.company.domain.entity.DirectorateEntity;
import com.mycompany.company.domain.entity.EmployeeEntity;
import com.mycompany.company.repository.DepartmentRepository;
import com.mycompany.company.repository.DirectorateRepository;
import com.mycompany.company.repository.EmployeeRepository;
import com.mycompany.company.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CompanyBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final DirectorateRepository directorateRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyBootstrap(DirectorateRepository directorateRepository, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.directorateRepository = directorateRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initEntities();
    }

    public void initEntities() {
        Set<Role> roles = new HashSet<>();
        Role roleAdmin = new Role();
        roleAdmin.setRoleName(RoleEnum.ADMIN);
        roleAdmin.setRoleDescription("Admin role");
        roleRepository.save(roleAdmin);
        roles.add(roleAdmin);

        DirectorateEntity directorate = new DirectorateEntity();
        directorate.setName("Education name");
        directorate.setDescription("Education description");
        directorateRepository.save(directorate);

        EmployeeEntity employeeMitrovic = EmployeeEntity.builder()
                .age(33)
                .firstName("Stefan")
                .familyName("Mitrovic")
                .username("mitrovic")
                .password(getEncodedPassword("password"))
                .roles(roles)
                .directorate(directorate)
                .pin("3223")
                .position(Position.HEAD_OF_DEPARTMENT)
                .build();
        employeeRepository.save(employeeMitrovic);
        directorateRepository.save(directorate);

//      *********************************************************************************

        DirectorateEntity directorateEntity = new DirectorateEntity();
        directorateEntity.setName("directorate name");
        directorateEntity.setDescription("directorate description");
        directorateEntity.setDirector(employeeMitrovic);
        directorateRepository.save(directorate);

        DepartmentEntity departmentEntity = DepartmentEntity.builder()
                .name("Department name")
                .description("department description")
                .directorate(directorate)
                .build();
        departmentRepository.save(departmentEntity);


        Set<Role> roles1 = new HashSet<>();
        Role roleUser = new Role();
        roleUser.setRoleName(RoleEnum.USER);
        roleUser.setRoleDescription("User role");
        roleRepository.save(roleUser);
        roles1.add(roleUser);

        EmployeeEntity employeeSavic = EmployeeEntity.builder()
                .age(33)
                .firstName("Savic")
                .familyName("Savic")
                .username("savic")
                .password(getEncodedPassword("password"))
                .roles(roles1)
                .pin("3223232323")
                .department(departmentEntity)
                .position(Position.EMPLOYEE)
                .build();
        employeeRepository.save(employeeSavic);

        EmployeeEntity employeeDjovan = EmployeeEntity.builder()
                .age(33)
                .firstName("djovan")
                .familyName("djovan")
                .username("djovan")
                .password(getEncodedPassword("password"))
                .roles(roles1)
                .department(departmentEntity)
                .pin("322323")
                .position(Position.EMPLOYEE)
                .build();
        employeeRepository.save(employeeDjovan);


    }


    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
