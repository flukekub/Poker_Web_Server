package com.pkpj.temp.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "chips")
    private BigDecimal chips;

    @Column(name = "role")
    private String role;
    
    @Column(name = "password")
    private String password;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    // mappedBy refers to the "tableId" field in the TablePlayer class
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TablePlayer> tables;
}
