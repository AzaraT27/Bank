package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sts.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);
}
