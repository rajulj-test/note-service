package com.bluestaq.notes.controller;

import com.bluestaq.notes.dto.GlobalNoteSearchDto;
import com.bluestaq.notes.model.Note;
import com.bluestaq.notes.model.User;
import com.bluestaq.notes.repository.NoteRepository;
import com.bluestaq.notes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User registration) {
        Optional<User> existing = userRepository.findByEmail(registration.getEmail());
        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }
    
        User user = new User();
        user.setName(registration.getName());
        user.setEmail(registration.getEmail());
        user.setTeamName(registration.getTeamName());
        user.setToken(UUID.randomUUID().toString()); // Triggers @PrePersist to generate hash
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/notes")
    public ResponseEntity<?> getNotes(
            @RequestParam(value = "token", required = false) String token, 
            @RequestParam(value = "shareToken", required = false) String shareToken) {
        
        Optional<User> userOpt;
        boolean isOwner = false;

        if (token != null && !token.isEmpty()) {
            userOpt = userRepository.findByToken(token);
            isOwner = true;
        } else if (shareToken != null && !shareToken.isEmpty()) {
            userOpt = userRepository.findByShareToken(shareToken);
        } else {
            return ResponseEntity.status(401).body("Missing token or shareToken");
        }

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        User user = userOpt.get();
        List<Note> notes = noteRepository.findByOwnerId(user.getId());

        return ResponseEntity.ok(new WorkspaceResponse(user.getName(), user.getTeamName(), isOwner, notes));
    }

    @PostMapping("/notes")
    public ResponseEntity<Note> createNote(@RequestParam(value = "token") String token, @RequestBody Note newNote) {
        // STRICT: Only raw owner token can create notes
        Optional<User> userOpt = userRepository.findByToken(token);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(403).build(); // Forbidden for public viewers
        }
        newNote.setOwner(userOpt.get());
        Note saved = noteRepository.save(newNote);
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping("/notes/search")
    public ResponseEntity<List<GlobalNoteSearchDto>> globalSearch(
            @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(noteRepository.globalSearch(keyword));
    }
    
 // Public directory of all workspaces
    @GetMapping("/workspaces")
    public ResponseEntity<List<PublicWorkspaceDto>> getAllWorkspaces() {
        List<PublicWorkspaceDto> workspaces = userRepository.findAll().stream()
                .map(u -> new PublicWorkspaceDto(u.getName(), u.getTeamName(), u.getShareToken()))
                .toList();
        return ResponseEntity.ok(workspaces);
    }

    // Safe DTO so owner tokens are never exposed publicly
    public record PublicWorkspaceDto(String name, String teamName, String shareToken) {}

    // Helper wrapper record for the workspace payload
    public record WorkspaceResponse(String name, String teamName, boolean isOwner, List<Note> notes) {}
}