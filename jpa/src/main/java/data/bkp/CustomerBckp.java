package data.bkp;

import org.template.demo.account.Account;
import org.template.demo.data.BaseEntity;

import javax.persistence.*;

//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "customer")
public class CustomerBckp extends BaseEntity {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Long customerId;

 private String firstName;

 private String lastName;

 private String email;

 @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
 @JoinColumn(name = "account_id")
 private Account account;

 public CustomerBckp(String firstName, String lastName, String email,
                     Account account) {
  this.firstName = firstName;
  this.lastName = lastName;
  this.email = email;
  this.account = account;
 }
}
