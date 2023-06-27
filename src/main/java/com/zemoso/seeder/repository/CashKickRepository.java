package com.zemoso.seeder.repository;

import com.zemoso.seeder.entity.CashKick;
import com.zemoso.seeder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CashKickRepository extends JpaRepository<CashKick, Long> {

}
