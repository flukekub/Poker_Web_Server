package com.pkpj.temp.repositories;

import com.pkpj.temp.entities.GameTable;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameTableRepository extends JpaRepository<GameTable,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<GameTable> findById(Long id);

    Optional<GameTable> findByTable_TableId(Long tableId);
}
