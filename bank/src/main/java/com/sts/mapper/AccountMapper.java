package com.sts.mapper;

import com.sts.dto.AccountDto;
import com.sts.entity.Account;

public class AccountMapper {
	public static Account mapToAccount(AccountDto accDto) {
		Account acc = new Account(
				accDto.getId(),
				accDto.getAccholderName(),
				accDto.getBalance()
		);
		return acc;
	}
	
	public static AccountDto mapToAccDto(Account acc) {
		AccountDto accountDto = new AccountDto(
				acc.getId(),
				acc.getAccholderName(),
				acc.getBalance()
		);	
		return accountDto;
	}
}
