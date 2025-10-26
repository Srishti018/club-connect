package com.cbit.club_connect.clubconnect.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "registration",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"}))
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Timestamp regDate;

    private String status; // e.g., PENDING, APPROVED, REJECTED

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Timestamp getRegDate() { return regDate; }
    public void setRegDate(Timestamp regDate) { this.regDate = regDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}