package com.amar.account.service.impl;

import com.amar.account.constants.AccountConstants;
import com.amar.account.dto.AccountDto;
import com.amar.account.dto.CustomerDto;
import com.amar.account.entity.Accounts;
import com.amar.account.entity.CustomerEntity;
import com.amar.account.exceptionHandler.CustomerAlreadyExistsException;
import com.amar.account.exceptionHandler.CustomerNotFoundException;
import com.amar.account.mapper.AccountsMapper;
import com.amar.account.mapper.CustomerMapper;
import com.amar.account.repository.AccountRepository;
import com.amar.account.repository.CustomerRepository;
import com.amar.account.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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

    /**
     *
     * @param customerDto - CustomerDTO object
     */

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        Accounts accountsEntity= accountRepository.findByAccountNumber(customerDto.getAccountDto().getAccountNumber())
                .orElseThrow( () -> new CustomerNotFoundException("Account", "Account No", customerDto.getAccountDto().getAccountNumber().toString())
        );

        Accounts accountsModified = AccountsMapper.mapToAccounts(customerDto.getAccountDto(), accountsEntity);

        CustomerEntity customerEntitiy = customerRepository.findByCustomerId(accountsEntity.getCustomerId())
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer", "Customer Id", accountsEntity.getCustomerId().toString())
                );

        CustomerEntity customerEntityModified = CustomerMapper.mapToCustomer(customerDto, customerEntitiy);


        accountsModified.setCustomerId(accountsEntity.getCustomerId());
        customerEntityModified.setCustomerId(accountsEntity.getCustomerId());

        accountRepository.save(accountsModified);
        customerRepository.save(customerEntitiy);  // ** Save new record if not exist **
        isUpdated = true;

        return isUpdated;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        CustomerEntity customerEntity = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () -> new CustomerNotFoundException("Customer", "Mobile Number", mobileNumber)
                );

        Accounts accounts = accountRepository.findByCustomerId(customerEntity.getCustomerId())
                .orElseThrow(
                        () -> new CustomerNotFoundException("Account", "CustomerId", customerEntity.getCustomerId().toString())
                );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customerEntity, new CustomerDto());
        customerDto.setAccountDto(AccountsMapper.mapToAccountsDto(accounts, new AccountDto()));

        return customerDto;
    }

    @Override
    public Boolean deleteAccount(String mobileNumber) {
        Boolean isUpdated = false;
        CustomerEntity customerEntityByMobile = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () ->  new CustomerNotFoundException("Customer", "Mobile Number", mobileNumber)
                );

        Long customerId = customerEntityByMobile.getCustomerId();
        Accounts accountbyCustomerId = accountRepository.findByCustomerId(customerId)
                .orElseThrow(
                        () ->  new CustomerNotFoundException("Customer", "Mobile Number", mobileNumber)
                );

        accountRepository.delete(accountbyCustomerId);

        customerRepository.delete(customerEntityByMobile);

        isUpdated = true;
        return isUpdated;
    }


}
