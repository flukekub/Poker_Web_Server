package com.pkpj.temp.services;

import java.math.BigDecimal;
import java.util.List;

import com.pkpj.temp.dtos.auth.GetMeResponseDto;
import com.pkpj.temp.dtos.NotificationPayload;
import com.pkpj.temp.dtos.games.UpdateChipsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pkpj.temp.constant.Role;
import com.pkpj.temp.entities.User;
import com.pkpj.temp.repositories.UserRepository;


@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AuthenticationManager authManager;

    @Autowired
    private  JWTService jwtService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository userRepository, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.authManager = authManager;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(String name, String email, String profileImageUrl, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setProfileImageUrl(profileImageUrl);
        user.setChips(BigDecimal.ZERO);
        user.setPassword(passwordEncoder.encode(password)); // เข้ารหัสรหัสผ่าน
        user.setRole(Role.USER.name()); // กำหนดค่าเริ่มต้นสำหรับ role เป็น "USER"
        return userRepository.save(user);
    }

    public String verify(String name, String password) {
        System.out.println("Authentication successful: " );
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(name, password)
        );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(name);
        } else {
            return "Authentication failed for user: " + name;
        }

    }
    public User getUserFromToken(String token){
        String name = jwtService.getNameFromToken(token);
        if (name == null) {
            return null; // หรือจัดการ error ตามที่คุณต้องการ
        }
        return userRepository.findByName(name);
    }

    public GetMeResponseDto updateUserChips(UpdateChipsDto updateChipsDto) {
        Long userId = updateChipsDto.getUserId();
        BigDecimal amount = updateChipsDto.getAmount();
        String operation = updateChipsDto.getOperation();

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            BigDecimal newChips = user.getChips();

            if ("ADD".equalsIgnoreCase(operation)) {
                newChips = newChips.add(amount);
            } else if ("DEDUCT".equalsIgnoreCase(operation)) {
                if (amount.compareTo(user.getChips()) > 0) {
                    throw new IllegalArgumentException("Insufficient chips");
                }
                newChips = newChips.subtract(amount);
            } else {
                return null; // Invalid operation
            }

            user.setChips(newChips);
            userRepository.save(user);

            NotificationPayload payload = new NotificationPayload("USER_"+user.getId()+"_CHIPS_UPDATED", "User chips updated");
            messagingTemplate.convertAndSend("/topic/notifications", payload);
            return new GetMeResponseDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getProfileImageUrl(),
                    user.getChips(),
                    user.getCreatedAt()
            );
        }
        return null; // User not found
    }
    
}
