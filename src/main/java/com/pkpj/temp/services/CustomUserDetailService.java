package com.pkpj.temp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pkpj.temp.entities.User;
import com.pkpj.temp.entities.UserDetailImpl;
import com.pkpj.temp.repositories.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
       User user = userRepository.findByName(name);
       System.out.println("User found: " + user);
       return new UserDetailImpl(user);
    }
    
}
