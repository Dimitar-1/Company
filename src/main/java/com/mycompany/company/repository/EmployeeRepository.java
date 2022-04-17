package com.mycompany.company.repository;

import com.mycompany.company.domain.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    EmployeeEntity findUserByUsername(String username);

    List<EmployeeEntity> findAllByUsername(String username);
}
