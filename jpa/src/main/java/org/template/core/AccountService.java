package org.template.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.template.demo.account.Account;
import org.template.demo.account.AccountRepository;

import java.util.Optional;

@Component
public class AccountService implements IService{

    @Autowired
    private AccountRepository repository;


    @Override
    public Account getAccount(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Optional<Account> getAccount(String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }
}
