
package com.cbit.club_connect.clubconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "club") // Must match your actual table name
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id") // matches your MySQL table column
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "Head_id")
    private Long headId;

    @Column(name = "club_email", nullable = false, unique = true)
    private String clubEmail;

    @Column(name = "club_password", nullable = false)
    private String clubPassword;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getHeadId() { return headId; }
    public void setHeadId(Long headId) { this.headId = headId; }

    public String getClubEmail() { return clubEmail; }
    public void setClubEmail(String clubEmail) { this.clubEmail = clubEmail; }

    public String getClubPassword() { return clubPassword; }
    public void setClubPassword(String clubPassword) { this.clubPassword = clubPassword; }
}
