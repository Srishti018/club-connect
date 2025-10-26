package com.cbit.club_connect.clubconnect.entity;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date date;
    private Time time;
    private String venue;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}