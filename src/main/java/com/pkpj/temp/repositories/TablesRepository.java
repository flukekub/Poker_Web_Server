package com.pkpj.temp.repositories;


import com.pkpj.temp.entities.Tables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TablesRepository extends JpaRepository<Tables, Long> {
    public Tables[] findByGameType(String gameType);
    public Tables findByTableId(Long tableId);
}
