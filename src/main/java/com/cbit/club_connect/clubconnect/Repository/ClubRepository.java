package com.cbit.club_connect.clubconnect.Repository;

import com.cbit.club_connect.clubconnect.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    // find by email + password (plain text check, Option A)
    Optional<Club> findByClubEmailIgnoreCaseAndClubPassword(String clubEmail, String clubPassword);

    // (optional) find by email only
    Optional<Club> findByClubEmailIgnoreCase(String clubEmail);
}
