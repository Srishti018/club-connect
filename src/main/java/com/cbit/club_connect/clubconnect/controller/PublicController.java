package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.ClubRepository;
import com.cbit.club_connect.clubconnect.Repository.EventRepository;
import com.cbit.club_connect.clubconnect.entity.Club;
import com.cbit.club_connect.clubconnect.entity.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicController {

    private final ClubRepository clubRepository;
    private final EventRepository eventRepository;

    public PublicController(ClubRepository clubRepository, EventRepository eventRepository) {
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

    /* ---------- CLUBS ---------- */
    @GetMapping("/clubs")
    public ResponseEntity<List<ClubDto>> getClubs() {
        List<ClubDto> clubs = clubRepository.findAll()
                .stream()
                .map(c -> new ClubDto(c.getId(), c.getName(), c.getDescription()))
                .toList();
        return ResponseEntity.ok(clubs);
    }

    public record ClubDto(Long id, String name, String description) {}

    /* ---------- EVENTS ---------- */
    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("DEBUG: Fetching events with start_at after " + now);

        List<Event> events = eventRepository.findByStartAtAfterOrderByStartAtAsc(now);

        if (events.isEmpty()) {
            System.out.println("DEBUG: No upcoming events found!");
        } else {
            System.out.println("DEBUG: Found " + events.size() + " upcoming event(s)");
        }

        List<EventDto> result = events.stream()
                .map(this::toEventDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    /* ---------- SINGLE EVENT BY ID ---------- */
    @GetMapping("/events/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(this::toEventDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ---------- DTO MAPPER ---------- */
    private EventDto toEventDto(Event e) {
        String clubName = null;
        if (e.getClub() != null) {
            clubName = e.getClub().getName();
        } else if (e.getClubId() != null) {
            Club club = clubRepository.findById(e.getClubId()).orElse(null);
            if (club != null) clubName = club.getName();
        }

        return new EventDto(
                e.getId(),
                e.getName(),
                e.getVenue(),
                e.getStartAt() != null ? e.getStartAt().toString() : null,
                clubName,
                e.getActivityPoints(),
                e.getDescription() // 👈 include description
        );
    }

    /* ---------- RECORDS ---------- */
    public record EventDto(
            Long id,
            String title,
            String venue,
            String dateTime,
            String clubName,
            Integer activityPoints,
            String description // 👈 added
    ) {}
}



