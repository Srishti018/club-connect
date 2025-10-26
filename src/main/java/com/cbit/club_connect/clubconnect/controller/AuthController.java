package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.UserRepository;
import com.cbit.club_connect.clubconnect.Repository.ClubRepository;
import com.cbit.club_connect.clubconnect.entity.User;
import com.cbit.club_connect.clubconnect.entity.Club;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public AuthController(UserRepository userRepository, ClubRepository clubRepository) {
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User body) {
        String email = norm(body.getEmail());
        String pw    = safe(body.getPassword());
        if (email.isEmpty() || pw.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and password required");
        }
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }
        User u = new User();
        u.setFirstName(safe(body.getFirstName()));
        u.setLastName(safe(body.getLastName()));
        u.setEmail(email);
        u.setPassword(pw);  // plain for prototype
        u.setRole("USER");
        u.setStatus("ACTIVE");
        return ResponseEntity.ok(userRepository.save(u));
    }

    public static class LoginRequest {
        private String email, password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    public static class LoginResponse {
        private Long userId, clubId; private String name, role;
        public LoginResponse(Long userId, Long clubId, String name, String role) {
            this.userId=userId; this.clubId=clubId; this.name=name; this.role=role;
        }
        public Long getUserId() { return userId; }
        public Long getClubId() { return clubId; }
        public String getName() { return name; }
        public String getRole() { return role; }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        String email = norm(body.getEmail());
        String pw    = safe(body.getPassword());
        if (email.isEmpty() || pw.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email and password required");
        }

        // 1) CLUBS table first
        Optional<Club> cEmail = clubRepository.findByClubEmailIgnoreCase(email);
        if (cEmail.isPresent()) {
            Club c = cEmail.get();
            if (pw.equals(safe(c.getClubPassword()))) {
                String display = c.getName() != null ? c.getName() : "Club Head";
                return ResponseEntity.ok(new LoginResponse(null, c.getId(), display, "CLUB_HEAD"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong club password");
            }
        }

        // 2) USERS table
        Optional<User> uOpt = userRepository.findByEmailIgnoreCase(email);
        if (uOpt.isPresent()) {
            User u = uOpt.get();
            if (Objects.equals(u.getPassword(), pw)) {
                String display = safe(u.getFirstName()).isEmpty() ? u.getEmail() : u.getFirstName();
                String role = (u.getRole() == null || u.getRole().isBlank()) ? "USER" : u.getRole();
                return ResponseEntity.ok(new LoginResponse(u.getId(), null, display, role));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong user password");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No account found for that email");
    }

    private String norm(String s){ return s==null? "" : s.trim().toLowerCase(); }
    private String safe(String s){ return s==null? "" : s.trim(); }
}
