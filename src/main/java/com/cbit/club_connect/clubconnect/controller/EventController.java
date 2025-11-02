package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.EventRepository;
import com.cbit.club_connect.clubconnect.entity.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /* ---------- CREATE EVENT (used by Club Dashboard) ---------- */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Event e) {
        if (e.getName() == null || e.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Event name is required");
        }
        if (e.getClubId() == null) {
            return ResponseEntity.badRequest().body("Club ID is required");
        }
        if (e.getStartAt() == null) {
            e.setStartAt(LocalDateTime.now());
        }
        if (e.getDescription() == null || e.getDescription().isEmpty()) {
            e.setDescription("No description provided.");
        }

        Event saved = eventRepository.save(e);
        return ResponseEntity.ok(saved);
    }

    /* ---------- UPDATE EVENT ---------- */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Event body) {
        Optional<Event> opt = eventRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(404).body("Event not found");

        Event e = opt.get();
        if (body.getName() != null) e.setName(body.getName());
        if (body.getVenue() != null) e.setVenue(body.getVenue());
        if (body.getStartAt() != null) e.setStartAt(body.getStartAt());
        if (body.getActivityPoints() != null) e.setActivityPoints(body.getActivityPoints());
        if (body.getDescription() != null) e.setDescription(body.getDescription());

        return ResponseEntity.ok(eventRepository.save(e));
    }

    /* ---------- DELETE EVENT ---------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!eventRepository.existsById(id))
            return ResponseEntity.status(404).body("Event not found");
        eventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /* ---------- GET ALL EVENTS BY CLUB ---------- */
    @GetMapping("/by-club/{clubId}")
    public ResponseEntity<?> getByClub(@PathVariable Long clubId) {
        List<Event> events = eventRepository.findByClubIdOrderByStartAtAsc(clubId);
        return ResponseEntity.ok(events);
    }

    /* ---------- GET UPCOMING EVENTS (for Students) ---------- */
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcoming() {
        List<Event> events = eventRepository.findByStartAtAfterOrderByStartAtAsc(LocalDateTime.now());
        return ResponseEntity.ok(events);
    }

    /* ---------- GET PAST EVENTS (for Club Dashboard) ---------- */
    @GetMapping("/past/by-club/{clubId}")
    public ResponseEntity<?> getPastEventsByClub(@PathVariable Long clubId) {
        List<Event> events = eventRepository.findByClubIdAndStartAtBeforeOrderByStartAtDesc(clubId, LocalDateTime.now());
        return ResponseEntity.ok(events);
    }

    /* ---------- GET UPCOMING EVENTS FOR CLUB (OPTIONAL) ---------- */
    @GetMapping("/upcoming/by-club/{clubId}")
    public ResponseEntity<?> getUpcomingEventsByClub(@PathVariable Long clubId) {
        List<Event> events = eventRepository.findByClubIdAndStartAtAfterOrderByStartAtAsc(clubId, LocalDateTime.now());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Event not found"));
    }

}
