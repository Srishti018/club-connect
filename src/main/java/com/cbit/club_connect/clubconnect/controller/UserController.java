package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.entity.User;
import com.cbit.club_connect.clubconnect.entity.Club;
import com.cbit.club_connect.clubconnect.Repository.UserRepository;
import com.cbit.club_connect.clubconnect.Repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            // --- normalize role coming from the UI ---
            String normalizedRole = normalizeRole(user.getRole());

            // -------- STUDENT SIGNUP --------
            if ("STUDENT".equals(normalizedRole)) {
                user.setRole("STUDENT");           // enforce canonical value
                user.setStatus("ACTIVE");
                User saved = userRepository.save(user);
                return ResponseEntity.ok(saved);
            }

            // -------- CLUB HEAD SIGNUP (Option A: plain clubPassword) --------
            if ("CLUB_HEAD".equals(normalizedRole)) {
                String clubEmail = safe(user.getClubEmail()).toLowerCase();
                String clubPassword = safe(user.getClubPassword());

                if (clubEmail.isEmpty() || clubPassword.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Missing club credentials.");
                }

                // Step 1: find club by email (ignore case)
                Optional<Club> clubOpt = clubRepository.findByClubEmailIgnoreCase(clubEmail);
                if (clubOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("No club found for that email.");
                }
                Club club = clubOpt.get();

                // Step 2: compare plain text password (Option A)
                if (!clubPassword.equals(safe(club.getClubPassword()))) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Wrong club password.");
                }

                // Guard: only one head per club
                if (club.getHeadId() != null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Club head already assigned.");
                }

                // Link & save
                user.setRole("CLUB_HEAD");          // enforce canonical value
                user.setRequestedClubId(club.getId());
                user.setStatus("ACTIVE");
                User savedUser = userRepository.save(user);

                club.setHeadId(savedUser.getId());
                clubRepository.save(club);

                return ResponseEntity.ok(savedUser);
            }

            // Any other role is invalid on public signup
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid role type. Use STUDENT or CLUB_HEAD.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during signup: " + e.getMessage());
        }
    }

    // ---- helpers ----
    private String safe(String s) { return s == null ? "" : s.trim(); }

    /**
     * Maps UI labels like "club head", "Club Head (Login with Club Credentials)"
     * to canonical "CLUB_HEAD". Also normalizes "student" -> "STUDENT".
     */
    private String normalizeRole(String raw) {
        String s = safe(raw).toUpperCase();
        // Replace spaces and punctuation with underscores to fold variants
        s = s.replaceAll("[^A-Z0-9]", "_");

        if ("STUDENT".equals(s)) return "STUDENT";
        if ("CLUB_HEAD".equals(s)) return "CLUB_HEAD";

        // Fallback: if it contains both CLUB and HEAD, treat as CLUB_HEAD
        if (s.contains("CLUB") && s.contains("HEAD")) return "CLUB_HEAD";

        return s; // will be rejected later if not recognized
    }
}

