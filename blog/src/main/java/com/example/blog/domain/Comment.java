package com.example.blog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Post post;

    @NotBlank
    @Size(max = 80)
    private String authorName;

    @Email
    @NotBlank
    @Size(max = 120)
    private String authorEmail;

    @NotBlank
    @Lob
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
}
