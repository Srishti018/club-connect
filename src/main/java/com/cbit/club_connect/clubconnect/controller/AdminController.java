
package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.UserRepository;
import com.cbit.club_connect.clubconnect.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // View all pending club head requests
    @GetMapping("/pending")
    public List<User> getPendingClubHeadRequests() {
        return userRepository.findAll().stream()
                .filter(u -> "PENDING_CLUB_HEAD".equalsIgnoreCase(u.getRole()))
                .toList();
    }

    // Approve a club head request
    @PostMapping("/approve/{id}")
    public String approveClubHead(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && "PENDING_CLUB_HEAD".equals(user.getRole())) {
            user.setRole("CLUB_HEAD");
            user.setStatus("ACTIVE");
            userRepository.save(user);
            return "✅ Approved successfully!";
        }
        return "❌ User not found or already approved.";
    }
}
