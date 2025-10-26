package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.entity.Attendance;
import com.cbit.club_connect.clubconnect.Repository.AttendanceRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;

    public AttendanceController(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @PostMapping
    public Attendance markAttendance(@RequestBody Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @GetMapping("/{id}")
    public Attendance getAttendanceById(@PathVariable Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteAttendance(@PathVariable Long id) {
        attendanceRepository.deleteById(id);
    }
}