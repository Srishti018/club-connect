package com.cbit.club_connect.clubconnect.Repository;

import com.cbit.club_connect.clubconnect.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Fetch the latest 20 registrations (sorted by ID descending)
    List<Registration> findTop20ByOrderByIdDesc();
    List<Registration> findByEventId(Long eventId);

}