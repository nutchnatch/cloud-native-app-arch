package org.template.core;

import org.template.demo.account.Account;

import java.util.Optional;

public interface IService {

    Account getAccount(Long id);

    Optional<Account> getAccount(String accountNumber);
}
