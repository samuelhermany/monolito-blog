package com.example.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequest(
        @NotBlank @Size(max=100) String name,
        @NotBlank @Email String email,
        @Size(max=255) String bio
) {}
