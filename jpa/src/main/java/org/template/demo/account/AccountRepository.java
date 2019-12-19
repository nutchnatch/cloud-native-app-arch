package org.template.demo.account;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AccountRepository extends
 PagingAndSortingRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);
}
