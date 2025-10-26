package com.cbit.club_connect.clubconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "club")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "club_email", nullable = false)
    private String clubEmail;

    @Column(name = "club_password", nullable = false)
    private String clubPassword;

    @Column(name = "head_id")
    private Long headId;


    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getClubEmail() { return clubEmail; }
    public void setClubEmail(String clubEmail) { this.clubEmail = clubEmail; }

    public String getClubPassword() { return clubPassword; }
    public void setClubPassword(String clubPassword) { this.clubPassword = clubPassword; }

    public Long getHeadId() { return headId; }
    public void setHeadId(Long headId) { this.headId = headId; }
}
