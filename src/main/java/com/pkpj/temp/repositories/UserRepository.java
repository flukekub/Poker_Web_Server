package com.pkpj.temp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pkpj.temp.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByName(String name);
}