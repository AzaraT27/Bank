package com.sts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AccountDto {

	private Long id;
	private String accholderName;
	private double balance;
}
