package com.cbit.club_connect.clubconnect.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Event_Id")
    private Long eventId;

    @Column(name = "event_name")         // denormalized, optional
    private String eventName;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "Reg_date", insertable = false, updatable = false)
    private Timestamp regDate;            // default CURRENT_TIMESTAMP in DB

    @Column(name = "status")
    private String status;

    @Column(name = "name")
    private String name;                  // <-- this was NULL earlier

    @Column(name = "email")
    private String email;

    @Column(name = "roll_num")
    private String rollNum;

    @Column(name = "year_of_study")
    private Integer yearOfStudy;

    @Column(name = "branch")
    private String branch;

    @Column(name = "section")
    private String section;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Timestamp getRegDate() { return regDate; }
    public void setRegDate(Timestamp regDate) { this.regDate = regDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRollNum() { return rollNum; }
    public void setRollNum(String rollNum) { this.rollNum = rollNum; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
}