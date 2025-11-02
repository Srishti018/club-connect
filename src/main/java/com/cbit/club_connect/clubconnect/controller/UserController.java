package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.AttendanceRepository;
import com.cbit.club_connect.clubconnect.entity.Attendance;
import com.cbit.club_connect.clubconnect.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final AttendanceRepository attendanceRepository;

    public UserController(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // ✅ Get users who attended or registered for a specific event
    @GetMapping("/by-event/{eventId}")
    public ResponseEntity<?> getUsersByEvent(@PathVariable Long eventId) {
        List<Attendance> attendanceList = attendanceRepository.findByEventId(eventId);
        List<User> users = attendanceList.stream()
                .map(Attendance::getUser)
                .filter(u -> u != null)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
