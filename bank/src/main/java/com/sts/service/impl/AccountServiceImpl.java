package com.sts.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sts.dto.AccountDto;
import com.sts.dto.TransactionDto;
import com.sts.dto.TransferFundDto;
import com.sts.entity.Account;
import com.sts.entity.Transaction;
import com.sts.exception.AccountException;
import com.sts.mapper.AccountMapper;
import com.sts.repository.AccountRepository;
import com.sts.repository.TransactionRepository;
import com.sts.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{

	private TransactionRepository transactionRepository;

	private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
	private static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
	private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";
	private AccountRepository accRepo;
	
	 public AccountServiceImpl(AccountRepository accountRepository,
             TransactionRepository transactionRepository) {
		 			this.accRepo = accountRepository;
		 			this.transactionRepository = transactionRepository;
}

	@Override
	public AccountDto createAccount(AccountDto accountDto) {
		Account acc = AccountMapper.mapToAccount(accountDto);
		Account saveacc = accRepo.save(acc);
		return AccountMapper.mapToAccDto(saveacc);
	}

	@Override
	public AccountDto getAccountById(Long id) {
		Account acc = accRepo.findById(id).
				orElseThrow(()->
				new AccountException("Account not found!!"));
		return AccountMapper.mapToAccDto(acc);
	}

	@Override
	public AccountDto deposit(Long id, double amount) {
		Account acc = accRepo.findById(id).
				orElseThrow(()->
				new AccountException("Account not found!!"));
		 
		double total = acc.getBalance() + amount;
		acc.setBalance(total);
		Account save = accRepo.save(acc);
		
		Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
		
		return AccountMapper.mapToAccDto(save);
	}

	@Override
	public AccountDto withdraw(Long id, double amount) {
		Account acc = accRepo.findById(id).
				orElseThrow(()->
				new AccountException("Account not found!!"));
		
		if(acc.getBalance()<amount) {
			throw new AccountException("Insufficient Amount");
		}
		double total = acc.getBalance() - amount;
		acc.setBalance(total);
		Account save = accRepo.save(acc);
		
		Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
		
		return AccountMapper.mapToAccDto(save);
	}

	@Override
	public List<AccountDto> getAll() {
		List<Account> all = accRepo.findAll();
		return all.stream()
		.map((account)->AccountMapper.mapToAccDto(account))
		.collect(Collectors.toList());
	}

	@Override
	public void delete(Long id) {
		Account acc = accRepo.findById(id).
				orElseThrow(()->
				new AccountException("Account not found!!"));
		
		accRepo.deleteById(id);
	}

	@Override
	public void transferFunds(TransferFundDto tfd) {
		//retrieve the acc from which we are sending the amount
		Account fromAccount = accRepo
			.findById(tfd.fromAccountId())
			.orElseThrow( () -> new AccountException("Account Not Found"));
		
		//retrieve the acc to send the amount
		Account toAccount = accRepo
				.findById(tfd.toAccountId())
				.orElseThrow( () -> new AccountException("Account Not Found"));
		
		if(fromAccount.getBalance() < tfd.amount()){
            throw new RuntimeException("Insufficient Amount");
		}
		
		//debit amount from fromAccount obj
		fromAccount.setBalance(fromAccount.getBalance()-tfd.amount());
		
		//credit amount to toAccount obj
		toAccount.setBalance(toAccount.getBalance()+tfd.amount());
		
		accRepo.save(fromAccount);
		accRepo.save(toAccount);
		
		Transaction transaction = new Transaction();
        transaction.setAccountId(tfd.fromAccountId());
        transaction.setAmount(tfd.amount());
        transaction.setTransactionType(TRANSACTION_TYPE_TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
	}

	@Override
	public List<TransactionDto> getAccountTransactions(Long accountId) {
		List<Transaction> transactions = transactionRepository
                .findByAccountIdOrderByTimestampDesc(accountId);

        return transactions.stream()
                .map((transaction) -> convertEntityToDto(transaction))
                .collect(Collectors.toList());
	}
	private TransactionDto convertEntityToDto(Transaction transaction){
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp()
        );
    }
}
