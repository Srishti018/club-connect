package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.EventRepository;
import com.cbit.club_connect.clubconnect.entity.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /* ---------- CREATE EVENT ---------- */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Event e) {
        if (e.getName() == null || e.getStartAt() == null) {
            return ResponseEntity.badRequest().body("name and startAt are required");
        }
        if (e.getDescription() == null) {
            e.setDescription("No description provided."); // 👈 default fallback
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
        if (body.getClubId() != null) e.setClubId(body.getClubId());
        if (body.getActivityPoints() != null) e.setActivityPoints(body.getActivityPoints());
        if (body.getDescription() != null) e.setDescription(body.getDescription()); // 👈 added support for updating description

        Event updated = eventRepository.save(e);
        return ResponseEntity.ok(updated);
    }

    /* ---------- DELETE EVENT ---------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) return ResponseEntity.status(404).body("Event not found");
        eventRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
