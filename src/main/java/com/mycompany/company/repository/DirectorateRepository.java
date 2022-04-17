package com.mycompany.company.repository;

import com.mycompany.company.domain.entity.DirectorateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorateRepository extends JpaRepository<DirectorateEntity, Long> {
}
