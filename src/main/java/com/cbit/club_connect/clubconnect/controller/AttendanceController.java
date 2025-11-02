package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.*;
import com.cbit.club_connect.clubconnect.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public AttendanceController(
            AttendanceRepository attendanceRepository,
            RegistrationRepository registrationRepository,
            EventRepository eventRepository,
            UserRepository userRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    // ✅ Fetch all attendance
    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // ✅ Save or update attendance
    @PostMapping
    public ResponseEntity<?> markAttendance(@RequestBody Attendance attendance) {
        try {
            Long eventId = attendance.getEvent() != null ? attendance.getEvent().getId() : null;
            Long userId = attendance.getUser() != null ? attendance.getUser().getId() : null;

            if (eventId == null) {
                return ResponseEntity.ok(Map.of("success", false, "reason", "missing event"));
            }

            Optional<Event> eventOpt = eventRepository.findById(eventId);
            if (eventOpt.isEmpty()) {
                return ResponseEntity.ok(Map.of("success", false, "reason", "invalid event"));
            }

            attendance.setEvent(eventOpt.get());

            if (userId != null) {
                userRepository.findById(userId).ifPresent(attendance::setUser);
            }

            // ✅ Check existing attendance
            List<Attendance> existing = attendanceRepository.findByEventIdAndUserId(eventId, userId);
            Attendance saved;
            if (!existing.isEmpty()) {
                Attendance old = existing.get(0);
                old.setPresent(attendance.getPresent());
                saved = attendanceRepository.save(old);
            } else {
                saved = attendanceRepository.save(attendance);
            }

            return ResponseEntity.ok(Map.of("success", true, "id", saved.getId()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("success", false, "reason", e.getMessage()));
        }
    }

    // ✅ Get attendance for an event
    @GetMapping("/event/{eventId}")
    public List<Map<String, Object>> getAttendanceByEvent(@PathVariable Long eventId) {
        List<Attendance> list = attendanceRepository.findByEventId(eventId);
        return list.stream().map(a -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", a.getId());
            map.put("present", a.getPresent());
            map.put("userId", a.getUser() != null ? a.getUser().getId() : null);
            map.put("eventId", a.getEvent() != null ? a.getEvent().getId() : null);
            return map;
        }).collect(Collectors.toList());
    }

    // ✅ Get registered students for an event
    @GetMapping("/event/{eventId}/registrations")
    public ResponseEntity<List<Map<String, Object>>> getRegisteredStudentsForEvent(@PathVariable Long eventId) {
        List<Registration> list = registrationRepository.findByEventId(eventId);

        List<Map<String, Object>> result = list.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("registrationId", r.getId());
            map.put("eventId", r.getEventId());
            map.put("name", r.getName());
            map.put("rollNum", r.getRollNum());
            map.put("email", r.getEmail());
            map.put("branch", r.getBranch());
            map.put("section", r.getSection());
            map.put("userId", r.getUserId());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}




