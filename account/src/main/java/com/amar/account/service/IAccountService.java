package com.amar.account.service;

import com.amar.account.dto.CustomerDto;
import jakarta.transaction.Transactional;

public interface IAccountService {
    /**
     *
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);

    boolean updateAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    @Transactional
    Boolean deleteAccount(String mobileNumber);
}
