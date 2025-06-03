package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Balance;
import com.matrix.Java._Spring.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository  extends JpaRepository<Balance, Long> {

    Balance findByUser(User user);
}
