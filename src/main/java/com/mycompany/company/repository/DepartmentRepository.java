package com.mycompany.company.repository;

import com.mycompany.company.domain.entity.DepartmentEntity;
import com.mycompany.company.domain.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

}
