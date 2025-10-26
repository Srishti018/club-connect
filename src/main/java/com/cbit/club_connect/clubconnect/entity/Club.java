
package com.cbit.club_connect.clubconnect.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "club")   // <-- IMPORTANT: match your real table name
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")                 // as shown in your screenshot
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "Head_id")           // note the capital H in your table
    private Long headId;

    @Column(name = "club_email", nullable = false, unique = true)
    private String clubEmail;

    @Column(name = "club_password", nullable = false)
    private String clubPassword;

    // getters/setters
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
