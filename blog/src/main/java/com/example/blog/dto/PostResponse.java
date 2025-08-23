package com.example.blog.dto;

import com.example.blog.domain.PostStatus;

public record PostResponse(
    Long id,
    String title,
    String slug,
    String content,
    String authorName,
    String categoryName,
    PostStatus status
) {}
