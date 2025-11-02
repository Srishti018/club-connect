package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.EventRepository;
import com.cbit.club_connect.clubconnect.Repository.RegistrationRepository;
import com.cbit.club_connect.clubconnect.Repository.UserRepository;
import com.cbit.club_connect.clubconnect.entity.Event;
import com.cbit.club_connect.clubconnect.entity.Registration;
import com.cbit.club_connect.clubconnect.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RegistrationController(RegistrationRepository registrationRepository,
                                  EventRepository eventRepository,
                                  UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /* --------- BROWSER-FRIENDLY GETs --------- */

    @GetMapping("/registrations/ping")
    public String ping() {
        return "ok";
    }

    @GetMapping("/registrations")
    public ResponseEntity<List<Registration>> listRecent() {
        return ResponseEntity.ok(registrationRepository.findTop20ByOrderByIdDesc());
    }

    /* --------------- POST (form submit) --------------- */

    @PostMapping(value = "/registrations", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest body) {
        try {
            if (body.eventId() == null) {
                return bad("eventId is required");
            }

            String name = firstNonBlank(body.name(), body.fullName());
            if (isBlank(name)) {
                return bad("name/fullName is required");
            }

            String roll = firstNonBlank(body.rollNum(), body.roll());
            if (isBlank(roll)) {
                return bad("rollNum/roll is required");
            }

            // 🔹 Determine user_id using email if not explicitly provided
            Long userId = body.userId();
            if (userId == null && body.email() != null) {
                Optional<User> existingUser = userRepository.findByEmailIgnoreCase(body.email());
                if (existingUser.isPresent()) {
                    userId = existingUser.get().getId();
                }
            }

            Integer year = body.yearOfStudy();
            if (year == null && !isBlank(body.year())) {
                try {
                    year = Integer.parseInt(body.year().trim());
                } catch (NumberFormatException ignore) {}
            }

            Registration r = new Registration();
            r.setEventId(body.eventId());
            r.setUserId(userId); // ✅ now this will not be null if email matches a user
            r.setStatus("REGISTERED");
            r.setName(name);
            r.setEmail(blankToNull(body.email()));
            r.setRollNum(roll);
            r.setYearOfStudy(year);
            r.setBranch(blankToNull(body.branch()));
            r.setSection(blankToNull(body.section()));

            String eventName = blankToNull(body.eventName());
            if (eventName == null) {
                eventName = eventRepository.findById(body.eventId())
                        .map(Event::getName)
                        .orElse(null);
            }
            r.setEventName(eventName);

            Registration saved = registrationRepository.save(r);
            return ResponseEntity.ok(saved);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + ex.getMessage());
        }
    }

    private static ResponseEntity<String> bad(String msg) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String blankToNull(String s) {
        return isBlank(s) ? null : s.trim();
    }

    private static String firstNonBlank(String... ss) {
        if (ss == null) return null;
        for (String s : ss) if (!isBlank(s)) return s.trim();
        return null;
    }

    public record RegistrationRequest(
            Long eventId,
            String eventName,
            Long userId,
            String name,
            String fullName,
            String email,
            String rollNum,
            String roll,
            Integer yearOfStudy,
            String year,
            String branch,
            String section
    ) {}
}
