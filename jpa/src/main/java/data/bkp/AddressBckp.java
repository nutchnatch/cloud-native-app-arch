package data.bkp;

import org.template.demo.account.Account;
import org.template.demo.address.AddressType;
import org.template.demo.data.BaseEntity;

import javax.persistence.*;

//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "address")
public class AddressBckp extends BaseEntity {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Long addressId;

 private String street1;

 private String street2;

 private String state;

 private String city;

 private String country;

 private Integer zipCode;

 @Enumerated(EnumType.STRING)
 private AddressType addressType;

 @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
 @JoinColumn(name="account_id")
 private Account account;

 public AddressBckp(String street1, String street2, String state, String city,
                    String country, AddressType addressType, Integer zipCode) {
  this.street1 = street1;
  this.street2 = street2;
  this.state = state;
  this.city = city;
  this.country = country;
  this.addressType = addressType;
  this.zipCode = zipCode;
 }

}
