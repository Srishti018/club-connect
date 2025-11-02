package com.cbit.club_connect.clubconnect.Repository; // (this is fine if you really use capital R)

import com.cbit.club_connect.clubconnect.entity.Event;  // <-- must match #1 exactly
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStartAtAfterOrderByStartAtAsc(LocalDateTime after);
    List<Event> findByClubIdOrderByStartAtAsc(Long clubId);
    List<Event> findByClubIdAndStartAtAfterOrderByStartAtAsc(Long clubId, LocalDateTime after);
    List<Event> findByClubIdAndStartAtBeforeOrderByStartAtDesc(Long clubId, LocalDateTime before);

}