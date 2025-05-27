package com.jay.url_shortner.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private String longUrl;

    private int visitCount;

    private LocalDateTime createdAt;
    private LocalDateTime expiresat;

    // Getters and Setters

}
