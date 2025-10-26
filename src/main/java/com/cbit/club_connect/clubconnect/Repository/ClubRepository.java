// ClubRepository.java
package com.cbit.club_connect.clubconnect.Repository;

import com.cbit.club_connect.clubconnect.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByClubEmailIgnoreCaseAndClubPassword(String clubEmail, String clubPassword);
    Optional<Club> findByClubEmailIgnoreCase(String clubEmail);

}
