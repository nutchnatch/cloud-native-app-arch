package org.template.demo.account;

import org.template.demo.address.Address;
import org.template.demo.creditcard.CreditCard;
import org.template.demo.customer.Customer;
import org.template.demo.data.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account extends BaseEntity {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Long accountId; // <1>

 private String accountNumber;

 // <2>
 @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="account")
 private Set<CreditCard> creditCards = new HashSet<>();

 @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="account")
// @JoinColumn(name = "account_id")
 private Set<Address> addresses = new HashSet<>();

// @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "account")
// private Customer customer;

 public Account(String accountNumber, Set<Address> addresses) {
  this.accountNumber = accountNumber;
  this.addresses.addAll(addresses);
 }

 public Account(String accountNumber) {
  this.accountNumber = accountNumber;
 }
}
