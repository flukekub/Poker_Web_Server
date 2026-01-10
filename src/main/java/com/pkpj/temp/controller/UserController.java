package com.pkpj.temp.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.pkpj.temp.dtos.GetMeResponseDto;
import com.pkpj.temp.dtos.LoginResponseDto;
import com.pkpj.temp.dtos.UpdateChipsDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pkpj.temp.dtos.UploadResultDto;
import com.pkpj.temp.entities.User;
import com.pkpj.temp.services.UserService;

@RestController
public class UserController {

    private final UserService userService;
    private final ProfileController profileController;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, ProfileController profileController) {
        this.userService = userService;
        this.profileController = profileController;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<User> createUser(
        @RequestPart("name") String name,
        @RequestPart("email") String email,
        @RequestPart("file") MultipartFile file,
        @RequestPart("password") String password
    ) throws IOException {

        String userId = UUID.randomUUID().toString();

        ResponseEntity<UploadResultDto> uploadResponse = profileController.uploadProfilePicture(file, userId); // คุณย้าย logic จาก ProfileController มาใช้ที่นี่
        if (!uploadResponse.getStatusCode().is2xxSuccessful()) {
            System.out.println("Upload failed with status: " + uploadResponse.getStatusCode());
            return ResponseEntity.status(uploadResponse.getStatusCode())
                .body(null); 
        }
        UploadResultDto uploadResultDto = uploadResponse.getBody();
        if (uploadResultDto == null || uploadResultDto.getUrl() == null) {
            System.out.println("Upload result is null or URL is null. UploadResultDto: " + uploadResultDto);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null); // หรือจัดการ error ตามที่คุณต้องการ
        }
        System.out.println("Upload successful! Image URL: " + uploadResultDto.getUrl());
       

        User saved = userService.createUser(name, email, uploadResultDto.getUrl(), password);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/api/auth/login")
    public LoginResponseDto login( @RequestPart("name") String name,
                        @RequestPart("password") String password) {

        String token =  userService.verify(name, password);
        String message = (token != null) ? "Login successful" : "Invalid credentials";
        return new LoginResponseDto(token,message);
    }
    @PostMapping("/api/auth/logout")
    public ResponseEntity<LoginResponseDto> logout() {
        // ในกรณี JWT ให้ client ลบ token ฝั่งตัวเอง
        return ResponseEntity.ok(new LoginResponseDto(null, "Logout successful"));
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<GetMeResponseDto> getMe(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7)
                : null;
        if( token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserFromToken(token);
        return ResponseEntity.ok( new GetMeResponseDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getProfileImageUrl(),
            user.getChips(),
            user.getCreatedAt()
        )
        );
    }
    @PostMapping("/api/auth/me/chips")
    public ResponseEntity<GetMeResponseDto> updateChips(@RequestBody UpdateChipsDto updateChipsDto) {
        logger.info("edit user chips: {}", updateChipsDto);
        GetMeResponseDto userDto = userService.updateUserChips(updateChipsDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/public/test")
    public ResponseEntity<String> testEndpoint() {
    return ResponseEntity.ok("Public endpoint is accessible");
}


}
