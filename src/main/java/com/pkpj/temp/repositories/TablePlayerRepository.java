package com.pkpj.temp.repositories;

import com.pkpj.temp.entities.TablePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TablePlayerRepository extends JpaRepository<TablePlayer,Long> {
    List<TablePlayer> findByTable_TableId(Long tableId);
}
