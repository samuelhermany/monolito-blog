package com.example.blog.dto;

import com.example.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
    @NotBlank @Size(max = 160) String title,
    @NotBlank String content,
    @NotNull Long authorId,
    @NotNull Long categoryId,
    PostStatus status
) {}
