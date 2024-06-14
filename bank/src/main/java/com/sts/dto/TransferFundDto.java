package com.sts.dto;

public record TransferFundDto(Long fromAccountId,
        Long toAccountId,
        double amount) {
}