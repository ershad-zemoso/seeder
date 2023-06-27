package com.zemoso.seeder.repository;

import com.zemoso.seeder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
