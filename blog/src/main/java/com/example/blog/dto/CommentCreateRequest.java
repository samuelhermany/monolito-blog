package com.example.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotBlank @Size(max=80) String authorName,
        @Email @Size(max=120) String authorEmail,
        @NotBlank String content
) {}
