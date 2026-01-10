package com.pkpj.temp.repositories;

import com.pkpj.temp.entities.PlayerActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerActionRepository extends JpaRepository<PlayerActionEntity,Long> {
    long countByHandHandId(Long handId);
}
