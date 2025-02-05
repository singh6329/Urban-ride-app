package com.application.urbanRide.repositories;

import com.application.urbanRide.entities.WalletTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionsRepository extends JpaRepository<WalletTransactions,Long> {
}
