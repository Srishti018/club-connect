package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.entity.User;
import com.cbit.club_connect.clubconnect.entity.Club;
import com.cbit.club_connect.clubconnect.Repository.UserRepository;
import com.cbit.club_connect.clubconnect.Repository.ClubRepository;

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

    /* ========================= STUDENT SIGNUP ========================= */
    @PostMapping("/signup/student")
    public ResponseEntity<?> studentSignup(@RequestBody User body) {
        String email = norm(body.getEmail());
        String pw = safe(body.getPassword());
        String first = safe(body.getFirstName());
        String last = safe(body.getLastName());

        if (email.isEmpty() || pw.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and password required");
        }

        // Check duplicate email across both tables
        if (userRepository.findByEmailIgnoreCase(email).isPresent() ||
                clubRepository.findByClubEmailIgnoreCase(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }

        User u = new User();
        u.setFirstName(first);
        u.setLastName(last);
        u.setEmail(email);
        u.setPassword(pw);
        u.setRole("STUDENT");
        u.setStatus("ACTIVE");
        userRepository.save(u);

        return ResponseEntity.ok(new LoginResponse(u.getId(), null, first.isEmpty() ? email : first, "STUDENT"));
    }

    /* ========================= CLUB SIGNUP ========================= */
    @PostMapping("/signup/club")
    public ResponseEntity<?> clubSignup(@RequestBody Club body) {
        Long clubId = body.getId();
        String name = safe(body.getName());
        String email = norm(body.getClubEmail());
        String pw = safe(body.getClubPassword());
        String desc = safe(body.getDescription());

        if (email.isEmpty() || pw.isEmpty() || name.isEmpty() || clubId == null) {
            return ResponseEntity.badRequest().body("Club ID, name, email, and password required");
        }

        // Prevent duplicates
        if (clubRepository.findByClubEmailIgnoreCase(email).isPresent() ||
                userRepository.findByEmailIgnoreCase(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }

        Club c = new Club();
        c.setId(clubId);
        c.setName(name);
        c.setClubEmail(email);
        c.setClubPassword(pw);
        c.setDescription(desc);
        clubRepository.save(c);

        return ResponseEntity.ok(new LoginResponse(null, c.getId(), c.getName(), "CLUB"));
    }

    /* ========================= LOGIN (BOTH) ========================= */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        String email = norm(body.getEmail());
        String pw = safe(body.getPassword());

        if (email.isEmpty() || pw.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email and password required");
        }

        // Try Club first
        Optional<Club> clubOpt = clubRepository.findByClubEmailIgnoreCase(email);
        if (clubOpt.isPresent()) {
            Club c = clubOpt.get();
            if (pw.equals(c.getClubPassword())) {
                return ResponseEntity.ok(new LoginResponse(null, c.getId(), c.getName(), "CLUB"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong club password");
            }
        }

        // Try Student
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            if (Objects.equals(u.getPassword(), pw)) {
                return ResponseEntity.ok(new LoginResponse(u.getId(), null, u.getFirstName(), "STUDENT"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong user password");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No account found for that email");
    }

    /* ========================= HELPERS ========================= */
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private Long userId;
        private Long clubId;
        private String name;
        private String role;

        public LoginResponse(Long userId, Long clubId, String name, String role) {
            this.userId = userId;
            this.clubId = clubId;
            this.name = name;
            this.role = role;
        }

        public Long getUserId() { return userId; }
        public Long getClubId() { return clubId; }
        public String getName() { return name; }
        public String getRole() { return role; }
    }

    private String norm(String s) { return s == null ? "" : s.trim().toLowerCase(); }
    private String safe(String s) { return s == null ? "" : s.trim(); }
}

