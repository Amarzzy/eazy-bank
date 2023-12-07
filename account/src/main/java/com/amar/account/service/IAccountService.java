package com.amar.account.service;

import com.amar.account.dto.CustomerDto;

public interface IAccountService {
    /**
     *
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);
}
