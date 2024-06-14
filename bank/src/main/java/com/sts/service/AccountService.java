package com.sts.service;


import java.util.List;

import com.sts.dto.AccountDto;
import com.sts.dto.TransactionDto;
import com.sts.dto.TransferFundDto;


public interface AccountService {
	AccountDto createAccount(AccountDto accountDto);
	AccountDto getAccountById(Long id);
	AccountDto deposit(Long id,double amount);
	AccountDto withdraw(Long id,double amount);
	List<AccountDto> getAll();
	void delete(Long id);
	void transferFunds(TransferFundDto tfd);
	List<TransactionDto> getAccountTransactions(Long accountid);
}
