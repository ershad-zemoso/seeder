package com.zemoso.seeder.repository;

import com.zemoso.seeder.entity.Contract;
import com.zemoso.seeder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("select c from Contract c where c.user = ?1 and c.available=true")
    List<Contract> findAllAvailableByUserId(User user);

    @Query("select c from Contract c where c.id in ?1 and c.available=true")
    List<Contract> findAllAvailableById(List<Long> contractIds);
}
