package com.cbit.club_connect.clubconnect.controller;

import com.cbit.club_connect.clubconnect.Repository.ClubRepository;
import com.cbit.club_connect.clubconnect.Repository.EventRepository;
import com.cbit.club_connect.clubconnect.entity.Club;
import com.cbit.club_connect.clubconnect.entity.Event;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /* ===================== Clubs ===================== */

    @GetMapping("/clubs")
    public List<ClubDto> getClubs() {
        return clubRepository.findAll()
                .stream()
                .map(this::toClubDto)
                .collect(Collectors.toList());
    }

    /* ===================== Events ===================== */

    // All upcoming events across clubs
    @GetMapping("/events/upcoming")
    public List<EventDto> getUpcomingEvents() {
        return eventRepository.findByStartAtAfterOrderByStartAtAsc(LocalDateTime.now())
                .stream()
                .map(this::toEventDtoWithClubName)
                .collect(Collectors.toList());
    }

    // Events for a specific club (upcoming by default)
    @GetMapping("/clubs/{clubId}/events")
    public List<EventDto> getEventsForClub(
            @PathVariable Long clubId,
            @RequestParam(defaultValue = "true") boolean upcomingOnly) {

        List<Event> events = upcomingOnly
                ? eventRepository.findByClubIdAndStartAtAfterOrderByStartAtAsc(clubId, LocalDateTime.now())
                : eventRepository.findByClubIdOrderByStartAtAsc(clubId);

        return events.stream()
                .map(this::toEventDtoWithClubName)
                .collect(Collectors.toList());
    }

    /* ===================== DTOs ===================== */

    public static class ClubDto {
        public Long id;
        public String name;
        public String description;
    }

    public static class EventDto {
        public Long id;
        public Long clubId;
        public String clubName;   // resolved via ClubRepository
        public String title;      // maps from events.name
        public String venue;
        public String dateTime;   // ISO string from start_at
        public String description; // optional (null for now)
    }

    /* ===================== Mappers ===================== */

    private ClubDto toClubDto(Club c) {
        ClubDto d = new ClubDto();
        d.id = c.getId();
        d.name = c.getName();
        d.description = c.getDescription(); // ensure Club has getDescription()
        return d;
    }

    private EventDto toEventDtoWithClubName(Event e) {
        EventDto d = new EventDto();
        d.id = e.getId();
        d.clubId = e.getClubId();
        d.title = e.getTitle();             // events.name -> Event.title in entity
        d.venue = e.getVenue();
        d.dateTime = e.getStartAt() != null ? e.getStartAt().toString() : null;
        d.description = null;               // set if you later add a description column
        d.clubName = getClubNameSafe(e.getClubId());
        return d;
    }

    private String getClubNameSafe(Long clubId) {
        if (clubId == null) return null;
        Optional<Club> c = clubRepository.findById(clubId);
        return c.map(Club::getName).orElse(null);
    }
}

