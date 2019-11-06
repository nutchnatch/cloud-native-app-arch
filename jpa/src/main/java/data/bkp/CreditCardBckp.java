package data.bkp;

import org.template.demo.account.Account;
import org.template.demo.creditcard.CreditCardType;
import org.template.demo.data.BaseEntity;

import javax.persistence.*;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "credit_card")
public class CreditCardBckp extends BaseEntity {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Long creditId;

 private String number;

 @Enumerated(EnumType.STRING)
 private CreditCardType type;

 @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
 @JoinColumn(name="account_id")
 private Account account;

 public CreditCardBckp(String number, CreditCardType type) {
  this.number = number;
  this.type = type;
 }
}
