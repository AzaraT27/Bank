package com.sts.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.dto.AccountDto;
import com.sts.dto.TransactionDto;
import com.sts.dto.TransferFundDto;
import com.sts.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private AccountService accountService;

	public AccountController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}
	
	@PostMapping
	public ResponseEntity<AccountDto> createacc(@RequestBody AccountDto accountDto){
		return new ResponseEntity<AccountDto>(accountService.createAccount(accountDto),HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> getAccById(@PathVariable Long id){
		AccountDto accdto = accountService.getAccountById(id);
		return ResponseEntity.ok(accdto);
	}
	
	@PutMapping("/{id}/deposit")
	public ResponseEntity<AccountDto> deposit(@PathVariable Long id,@RequestBody Map<String,Double> request){
		Double amt = request.get("amount");
		AccountDto accountDto = accountService.deposit(id, amt);
		return ResponseEntity.ok(accountDto);
	}
	
	@PutMapping("/{id}/withdraw")
	public ResponseEntity<AccountDto> withdraw(@PathVariable Long id,@RequestBody Map<String,Double> request){
		Double amt = request.get("amount");
		AccountDto accountDto = accountService.withdraw(id, amt);
		return ResponseEntity.ok(accountDto);
	}
	
	@GetMapping()
	public ResponseEntity<List<AccountDto>> getAll(){
		List<AccountDto> all = accountService.getAll();
		return ResponseEntity.ok(all);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAcc(@PathVariable Long id){
		accountService.delete(id);
		return ResponseEntity.ok("Deleted!");
	}
	
	//Build Transfer Rest API
	@PostMapping("/transfer")
    public ResponseEntity<String> transferFund(@RequestBody TransferFundDto transferFundDto){
        accountService.transferFunds(transferFundDto);
        return ResponseEntity.ok("Transfer Successful");
    }
	
	// Build transactions REST API
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> fetchAccountTransactions(@PathVariable("id") Long accountId){

        List<TransactionDto> transactions = accountService.getAccountTransactions(accountId);

        return ResponseEntity.ok(transactions);
    }
}
