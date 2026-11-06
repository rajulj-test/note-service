package com.bluestaq.notes.dto;

import java.time.LocalDateTime;

public record GlobalNoteSearchDto(
    Long id,
    String title,
    String content,
    LocalDateTime createdAt,
    String ownerName,
    String teamName,
    String shareToken
) {}