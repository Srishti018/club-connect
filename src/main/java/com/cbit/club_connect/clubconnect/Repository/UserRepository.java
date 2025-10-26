package com.cbit.club_connect.clubconnect.Repository;

import com.cbit.club_connect.clubconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
