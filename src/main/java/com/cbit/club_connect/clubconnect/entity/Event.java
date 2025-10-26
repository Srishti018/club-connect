package com.cbit.club_connect.clubconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event") // keep your real table name
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")              // capital I
    private Long id;

    @Column(name = "name", nullable = false)
    private String title;             // map to "name" column

    @Column(name = "Club_Id", nullable = false) // capital C + I
    private Long clubId;

    @Column(name = "venue")
    private String venue;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getClubId() { return clubId; }
    public void setClubId(Long clubId) { this.clubId = clubId; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
}

