package com.pkpj.temp.repositories;

import com.pkpj.temp.entities.HandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandRepository extends JpaRepository<HandEntity,Long> {
}
