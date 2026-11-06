package com.bluestaq.notes.repository;

import com.bluestaq.notes.dto.GlobalNoteSearchDto;
import com.bluestaq.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByOwnerId(Long ownerId);

    // Global database search across all notes, authors, and team names
    @Query("SELECT new com.bluestaq.notes.dto.GlobalNoteSearchDto(" +
           "n.id, n.title, n.content, n.createdAt, u.name, u.teamName, u.shareToken) " +
           "FROM Note n JOIN n.owner u WHERE " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.teamName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<GlobalNoteSearchDto> globalSearch(@Param("keyword") String keyword);
}