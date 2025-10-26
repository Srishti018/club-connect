package com.cbit.club_connect.clubconnect.Repository;

import com.cbit.club_connect.clubconnect.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByClubIdOrderByStartAtAsc(Long clubId);
    List<Event> findByClubIdAndStartAtAfterOrderByStartAtAsc(Long clubId, LocalDateTime after);
    List<Event> findByStartAtAfterOrderByStartAtAsc(LocalDateTime after);
}
