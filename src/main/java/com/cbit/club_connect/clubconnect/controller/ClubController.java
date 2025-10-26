package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.entity.Club;
import com.cbit.club_connect.clubconnect.Repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clubs")
@CrossOrigin(origins = "*")
public class ClubController {

    @Autowired
    private ClubRepository clubRepository;

    // Create a new club (admin only in real apps)
    @PostMapping
    public ResponseEntity<?> createClub(@RequestBody Club club) {
        if (club.getClubEmail() == null || club.getClubPassword() == null) {
            return ResponseEntity.badRequest().body("clubEmail and clubPassword are required.");
        }

        // normalize email
        club.setClubEmail(club.getClubEmail().trim().toLowerCase());

        // check if already exists
        if (clubRepository.findByClubEmailIgnoreCaseAndClubPassword(club.getClubEmail(), club.getClubPassword()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Club already exists.");
        }

        Club saved = clubRepository.save(club);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // List all clubs
    @GetMapping
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    // Get one club by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getClub(@PathVariable Long id) {
        Optional<Club> clubOpt = clubRepository.findById(id);
        return clubOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Club not found"));
    }

    // Set or clear headId for a club
    @PutMapping("/{id}/head")
    public ResponseEntity<?> setHead(@PathVariable Long id, @RequestBody Long headId) {
        Optional<Club> clubOpt = clubRepository.findById(id);
        if (clubOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Club not found");
        }
        Club club = clubOpt.get();
        club.setHeadId(headId); // can pass null to clear
        return ResponseEntity.ok(clubRepository.save(club));
    }
}
