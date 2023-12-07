package com.amar.account.service.impl;

import com.amar.account.constants.AccountConstants;
import com.amar.account.dto.CustomerDto;
import com.amar.account.entity.Accounts;
import com.amar.account.entity.CustomerEntity;
import com.amar.account.exceptionHandler.CustomerAlreadyExistsException;
import com.amar.account.mapper.CustomerMapper;
import com.amar.account.repository.AccountRepository;
import com.amar.account.repository.CustomerRepository;
import com.amar.account.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;

    /**
     * @param customerDto - CustomerDTO Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        CustomerEntity customer = CustomerMapper.mapToCustomer(customerDto, new CustomerEntity());
        if(customerRepository.findByMobileNumber(customer.getMobileNumber()).isPresent()){
            throw new CustomerAlreadyExistsException("Mobile Number already registered.");
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("SOMEBODY");
        CustomerEntity savedCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(savedCustomer));
    }


    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(CustomerEntity customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("SOMEBODY");
        return newAccount;
    }
}
