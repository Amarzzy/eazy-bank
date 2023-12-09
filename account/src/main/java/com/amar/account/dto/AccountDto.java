package com.amar.account.dto;

import jakarta.annotation.Nonnull;
import lombok.Data;

@Data
public class AccountDto {


    private Long accountNumber;

    private String accountType;

    private String branchAddress;

}
