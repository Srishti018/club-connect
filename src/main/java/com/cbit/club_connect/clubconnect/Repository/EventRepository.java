package com.cbit.club_connect.clubconnect.Repository;

import com.cbit.club_connect.clubconnect.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> { }